package com.projectBackend.project.crawling.crawlExchange;


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
public class CrawlExchangeSchedularService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final CrawlExchangeRepository crawlExchangeRepository;

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void performCrawling() throws JsonProcessingException {
        // Flask 애플리케이션의 stockTop 엔드포인트에 get 요청 보내기
        String url = "http://localhost:5000/python/exchange";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String data = response.getBody();

        if (data != null) {
            List<CrawlExchangeDto> dataDtoList = objectMapper.readValue(data, new TypeReference<List<CrawlExchangeDto>>() {
            });
            // 데이터 최신화를 위해 삭제
            crawlExchangeRepository.deleteAll();
            for (CrawlExchangeDto exchangeDto : dataDtoList) {
                Optional<CrawlExchangeEntity> existingEntityOptional = crawlExchangeRepository.findByCrawlExchangeName(exchangeDto.getName());
                if (existingEntityOptional.isPresent()) {
                    CrawlExchangeEntity crawlExchangeEntity = existingEntityOptional.get();
                    crawlExchangeEntity.setCrawlExchangeExchange(exchangeDto.getExchange());
                    crawlExchangeEntity.setCrawlExchangeName(exchangeDto.getName());
                    crawlExchangeEntity.setCrawlExchangeBuy(exchangeDto.getBuy());
                    crawlExchangeEntity.setCrawlExchangeReceive(exchangeDto.getReceive());
                    crawlExchangeEntity.setCrawlExchangeSell(exchangeDto.getSell());
                    crawlExchangeEntity.setCrawlExchangeSend(exchangeDto.getSend());
                    crawlExchangeEntity.setCrawlExchangeTbRate(exchangeDto.getTbRate());
                    crawlExchangeEntity.setCrawlExchangeTbRate(exchangeDto.getTbRate());
                }
                else {
                    CrawlExchangeEntity crawlExchangeEntity = CrawlExchangeEntity.builder()
                            .crawlExchangeName(exchangeDto.getName())
                            .crawlExchangeTbRate(exchangeDto.getTbRate())
                            .crawlExchangeBuy(exchangeDto.getBuy())
                            .crawlExchangeSell(exchangeDto.getSell())
                            .crawlExchangeSend(exchangeDto.getSend())
                            .crawlExchangeReceive(exchangeDto.getReceive())
                            .crawlExchangeExchange(exchangeDto.getExchange())
                            .crawlExchangeTbRate(exchangeDto.getTbRate())
                            .build();

                    crawlExchangeRepository.save(crawlExchangeEntity);
                }
            }
        }
    }

}
