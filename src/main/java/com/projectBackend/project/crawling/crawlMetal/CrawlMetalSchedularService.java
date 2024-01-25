package com.projectBackend.project.crawling.crawlMetal;

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
public class CrawlMetalSchedularService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final CrawlMetalRepository crawlMetalRepository;

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void performCrawling() throws JsonProcessingException {
        // Flask 애플리케이션의 metal 엔드포인트에 POST 요청 보내기
        String url = "http://localhost:5000/python/metal";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String data = response.getBody();

        if (data != null) {
            List<CrawlMetalDto> dataDtoList = objectMapper.readValue(data, new TypeReference<List<CrawlMetalDto>>() {});

            // 데이터 최신화를 위해 삭제
            crawlMetalRepository.deleteAll();

            for (CrawlMetalDto metalDto : dataDtoList) {
                // 이름으로 기존 데이터 조회
                Optional<CrawlMetalEntity> existingEntityOptional = crawlMetalRepository.findByCrawlMetalName(metalDto.getName());

                if (existingEntityOptional.isPresent()) {
                    // 기존 데이터가 있으면 업데이트
                    CrawlMetalEntity crawlMetalEntity = existingEntityOptional.get();
                    crawlMetalEntity.setCrawlMetalUnits(metalDto.getUnits());
                    crawlMetalEntity.setCrawlMetalPrice(metalDto.getPrice());
                    crawlMetalEntity.setCrawlMetalYesterday(metalDto.getYesterday());
                    crawlMetalEntity.setCrawlMetalRate(metalDto.getRate());
                    crawlMetalEntity.setCrawlMetalDate(metalDto.getDate());
                    crawlMetalEntity.setCrawlMetalExchange(metalDto.getExchange());

                    crawlMetalRepository.save(crawlMetalEntity);
                } else {
                    // 기존 데이터가 없으면 새로 추가
                    CrawlMetalEntity crawlMetalEntity = CrawlMetalEntity.builder()
                            .crawlMetalName(metalDto.getName())
                            .crawlMetalUnits(metalDto.getUnits())
                            .crawlMetalPrice(metalDto.getPrice())
                            .crawlMetalYesterday(metalDto.getYesterday())
                            .crawlMetalRate(metalDto.getRate())
                            .crawlMetalDate(metalDto.getDate())
                            .crawlMetalExchange(metalDto.getExchange())
                            .build();

                    crawlMetalRepository.save(crawlMetalEntity);
                }
            }
        }
    }
}
