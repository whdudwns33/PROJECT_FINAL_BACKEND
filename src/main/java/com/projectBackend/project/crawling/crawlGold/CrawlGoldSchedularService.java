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

        //assert 문은 주어진 조건이 참이면 계속 진행하고, 거짓이라면 AssertionError를 발생시켜 프로그램을 중단
        assert data != null;
        List<CrawlGoldDto> dataDtoList = objectMapper.readValue(data, new TypeReference<List<CrawlGoldDto>>() {});

        // 데이터 최신화를 위해 삭제
        crawlGoldRepository.deleteAll();

        for (CrawlGoldDto goldDto : dataDtoList) {
            CrawlGoldEntity crawlGoldEntity = CrawlGoldEntity.builder()
                    .CrawlGoldname(goldDto.getName())
                    .CrawlGoldunit(goldDto.getUnit())
                    .CrawlGoldprice(goldDto.getPrice())
                    .CrawlGoldyesterday(goldDto.getYesterday())
                    .CrawlGoldrate(goldDto.getRate())
                    .CrawlGolddate(goldDto.getDate())
                    .build();

            crawlGoldRepository.save(crawlGoldEntity);
        }
    }
}
