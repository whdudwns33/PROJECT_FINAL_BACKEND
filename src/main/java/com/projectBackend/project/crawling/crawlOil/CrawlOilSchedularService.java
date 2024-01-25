package com.projectBackend.project.crawling.crawlOil;

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
public class CrawlOilSchedularService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final CrawlOilRepository crawlOilRepository;

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void performCrawling() throws JsonProcessingException {
        // Flask 애플리케이션의 oil 엔드포인트에 GET 요청 보내기
        String url = "http://localhost:5000/python/oil";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String data = response.getBody();

        if (data != null) {
            List<CrawlOilDto> oilDtoList = objectMapper.readValue(data, new TypeReference<List<CrawlOilDto>>() {});

            // 데이터 최신화를 위해 삭제
            crawlOilRepository.deleteAll();

            for (CrawlOilDto oilDto : oilDtoList) {
                // 이름으로 기존 데이터 조회
                Optional<CrawlOilEntity> existingEntityOptional = crawlOilRepository.findByCrawlOilName(oilDto.getName());

                if (existingEntityOptional.isPresent()) {
                    // 기존 데이터가 있으면 업데이트
                    CrawlOilEntity crawlOilEntity = existingEntityOptional.get();
                    crawlOilEntity.setCrawlOilUnit(oilDto.getUnit());
                    crawlOilEntity.setCrawlOilPrice(oilDto.getPrice());
                    crawlOilEntity.setCrawlOilYesterday(oilDto.getYesterday());
                    crawlOilEntity.setCrawlOilRate(oilDto.getRate());

                    crawlOilRepository.save(crawlOilEntity);
                } else {
                    // 기존 데이터가 없으면 새로 추가
                    CrawlOilEntity crawlOilEntity = CrawlOilEntity.builder()
                            .crawlOilName(oilDto.getName())
                            .crawlOilUnit(oilDto.getUnit())
                            .crawlOilPrice(oilDto.getPrice())
                            .crawlOilYesterday(oilDto.getYesterday())
                            .crawlOilRate(oilDto.getRate())
                            .build();

                    crawlOilRepository.save(crawlOilEntity);
                }
            }
        }
    }
}
