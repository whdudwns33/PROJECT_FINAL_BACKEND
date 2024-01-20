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

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlArgSchedularService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final CrawlArgRepository crawlExchangeRepository;

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void performCrawling() throws JsonProcessingException {
        // Flask 애플리케이션의 stockTop 엔드포인트에 POST 요청 보내기
        String url = Common.python + "/arg";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String stockData = response.getBody();

        //assert 문은 주어진 조건이 참이면 계속 진행하고, 거짓이라면 AssertionError를 발생시켜 프로그램을 중단
        assert stockData != null;
        List<CrawlArgDto> stockDtoList = objectMapper.readValue(stockData, new TypeReference<List<CrawlArgDto>>() {});
        // 데이터 최신화를 위해 삭제
        crawlExchangeRepository.deleteAll();
        for( CrawlArgDto crawlExchangeDto : stockDtoList) {
            CrawlArgEntity crawlStockEntity = CrawlArgEntity.builder()
                    .CrawlExchangeName(crawlExchangeDto.getName())
                    .CrawlExchangeYesterday(crawlExchangeDto.getYesterday())
                    .CrawlExchangeExchange(crawlExchangeDto.getExchange())
                    .CrawlExchangeDate(crawlExchangeDto.getDate())
                    .CrawlExchangeMonth(crawlExchangeDto.getMonth())
                    .CrawlExchangePrice(crawlExchangeDto.getPrice())
                    .CrawlExchangeRate(crawlExchangeDto.getRate())
                    .CrawlExchangeUnits(crawlExchangeDto.getUnits())
                    .build();
            crawlExchangeRepository.save(crawlStockEntity);
        }
    }
}
