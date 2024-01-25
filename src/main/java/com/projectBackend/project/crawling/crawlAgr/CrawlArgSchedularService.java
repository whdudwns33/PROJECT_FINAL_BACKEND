package com.projectBackend.project.crawling.crawlAgr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectBackend.project.utils.Common;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlArgSchedularService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final CrawlArgRepository crawlExchangeRepository;

    // JPA 에서 save는 INSERT 와 UPDATE의 역할을 모두 다 한다.
    @Scheduled(fixedRate = 1000 * 60 * 60 )
    public void performCrawling() throws JsonProcessingException {
        // Flask 애플리케이션의 stockTop 엔드포인트에 POST 요청 보내기
        String url = Common.python + "/arg";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String data = response.getBody();

        if (data != null) {
            log.warn("CrawlArgSchedularService");
            List<CrawlArgDto> crawlArgDtoList = objectMapper.readValue(data, new TypeReference<List<CrawlArgDto>>() {
            });

            for (CrawlArgDto crawlArgDto : crawlArgDtoList) {
                // 이름으로 기존 데이터 조회
                Optional<CrawlArgEntity> existingEntityOptional = crawlExchangeRepository.findByCrawlExchangeName(crawlArgDto.getName());

                if (existingEntityOptional.isPresent()) {
                    // 기존 데이터가 있으면 업데이트
                    CrawlArgEntity existingEntity = existingEntityOptional.get();
                    existingEntity.setCrawlExchangeYesterday(crawlArgDto.getYesterday());
                    existingEntity.setCrawlExchangeExchange(crawlArgDto.getExchange());
                    existingEntity.setCrawlExchangeDate(crawlArgDto.getDate());
                    existingEntity.setCrawlExchangeMonth(crawlArgDto.getMonth());
                    existingEntity.setCrawlExchangePrice(crawlArgDto.getPrice());
                    existingEntity.setCrawlExchangeRate(crawlArgDto.getRate());
                    existingEntity.setCrawlExchangeUnits(crawlArgDto.getUnits());
                    // 즉각적으로 저장 반영
                    crawlExchangeRepository.saveAndFlush(existingEntity);
                } else {
                    // 기존 데이터가 없으면 새로 추가
                    CrawlArgEntity newEntity = CrawlArgEntity.builder()
                            .crawlExchangeName(crawlArgDto.getName())
                            .crawlExchangeYesterday(crawlArgDto.getYesterday())
                            .crawlExchangeExchange(crawlArgDto.getExchange())
                            .crawlExchangeDate(crawlArgDto.getDate())
                            .crawlExchangeMonth(crawlArgDto.getMonth())
                            .crawlExchangePrice(crawlArgDto.getPrice())
                            .crawlExchangeRate(crawlArgDto.getRate())
                            .crawlExchangeUnits(crawlArgDto.getUnits())
                            .build();

                    crawlExchangeRepository.save(newEntity);
                }
            }
        }
    }
}
