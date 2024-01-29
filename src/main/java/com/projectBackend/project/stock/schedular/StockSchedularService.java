package com.projectBackend.project.stock.schedular;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectBackend.project.stock.jpa.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockSchedularService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final StockService stockService;

    @Value("${flask.data.path}")
    private String flaskDataPath;

    @Scheduled(fixedRate = 1000 * 60 * 2)
    public void pullRequest() throws JsonProcessingException {
        // Flask 애플리케이션의 stock 엔드포인트에 새로운 데이터를 달라고 요청 보내기
        String url = "http://localhost:5000/python/stock/pull";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String stockData = response.getBody();

        // 여기에 받은 데이터를 처리하는 로직 추가
        log.info("Stock data received from Flask: {}", stockData);
    }
}
