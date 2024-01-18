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

        //assert 문은 주어진 조건이 참이면 계속 진행하고, 거짓이라면 AssertionError를 발생시켜 프로그램을 중단
        assert data != null;
        List<CrawlMetalDto> dataDtoList = objectMapper.readValue(data, new TypeReference<List<CrawlMetalDto>>() {});

        // 데이터 최신화를 위해 삭제
        crawlMetalRepository.deleteAll();

        for (CrawlMetalDto metalDto : dataDtoList) {
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
