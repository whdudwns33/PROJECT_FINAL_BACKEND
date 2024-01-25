package com.projectBackend.project.crawling.crawlGold;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class CrawlGoldSchedularService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final CrawlGoldRepository crawlGoldRepository;

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void performCrawling() throws JsonProcessingException {
        // Flask 애플리케이션의 gold 엔드포인트에 POST 요청 보내기
        String url = "http://localhost:5000/python/gold";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String data = response.getBody();

        if (data != null) {
            List<CrawlGoldDto> dataDtoList = objectMapper.readValue(data, new TypeReference<List<CrawlGoldDto>>() {});

            for (CrawlGoldDto goldDto : dataDtoList) {
                // 이름으로 기존 데이터 조회
                Optional<CrawlGoldEntity> existingEntityOptional = crawlGoldRepository.findByCrawlGoldName(goldDto.getName());

                if (existingEntityOptional.isPresent()) {
                    log.warn("CrawlGoldSchedularService");
                    // 기존 데이터가 있으면 업데이트
                    CrawlGoldEntity crawlGoldEntity = existingEntityOptional.get();
                    crawlGoldEntity.setCrawlGoldUnit(goldDto.getUnit());
                    crawlGoldEntity.setCrawlGoldPrice(goldDto.getPrice());
                    crawlGoldEntity.setCrawlGoldYesterday(goldDto.getYesterday());
                    crawlGoldEntity.setCrawlGoldRate(goldDto.getRate());
                    crawlGoldEntity.setCrawlGoldDate(goldDto.getDate());

                    crawlGoldRepository.save(crawlGoldEntity);
                } else {
                    // 기존 데이터가 없으면 새로 추가
                    CrawlGoldEntity crawlGoldEntity = CrawlGoldEntity.builder()
                            .crawlGoldName(goldDto.getName())
                            .crawlGoldUnit(goldDto.getUnit())
                            .crawlGoldPrice(goldDto.getPrice())
                            .crawlGoldYesterday(goldDto.getYesterday())
                            .crawlGoldRate(goldDto.getRate())
                            .crawlGoldDate(goldDto.getDate())
                            .build();

                    crawlGoldRepository.save(crawlGoldEntity);
                }
            }
        }
    }
}
