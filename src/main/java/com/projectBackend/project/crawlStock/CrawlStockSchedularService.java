package com.projectBackend.project.crawlStock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlStockSchedularService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final CrawlStockRpository crawlStockRpository;

    @Scheduled(fixedRate = 6000)
    public void performCrawling() throws JsonProcessingException {
        // Flask 애플리케이션의 stockTop 엔드포인트에 POST 요청 보내기
        String url = "http://localhost:5000/python/stock";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String stockData = response.getBody();
        System.out.println(stockData);
    }

}
