package com.projectBackend.project.crawling.crawlMarket;

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
public class CrawlMarketSchedularService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final CrawlMarketRepository crawlMarketRepository;

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void performCrawling() throws JsonProcessingException {
        // Flask 애플리케이션의 exchangeMarket 엔드포인트에 POST 요청 보내기
        String url = "http://localhost:5000/python/exchangeMarket";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String marketData = response.getBody();

        if (marketData != null) {
            List<CrawlMarketDto> marketDtoList = objectMapper.readValue(marketData, new TypeReference<List<CrawlMarketDto>>() {});

            for (CrawlMarketDto crawlMarketDto : marketDtoList) {
                // Symbol로 기존 데이터 조회
                Optional<CrawlMarketEntity> existingEntityOptional = crawlMarketRepository.findByCrawlMarketSymbol(crawlMarketDto.getSymbol());

                if (existingEntityOptional.isPresent()) {
                    // 기존 데이터가 있으면 업데이트
                    CrawlMarketEntity crawlMarketEntity = existingEntityOptional.get();
                    crawlMarketEntity.setCrawlMarketRate(crawlMarketDto.getRate());
                    crawlMarketEntity.setCrawlMarketBefore(crawlMarketDto.getBefore());
                    crawlMarketEntity.setCrawlMarketCurrent(crawlMarketDto.getCurrent());
                    crawlMarketEntity.setCrawlMarketName(crawlMarketDto.getName());

                    crawlMarketRepository.save(crawlMarketEntity);
                } else {
                    // 기존 데이터가 없으면 새로 추가
                    CrawlMarketEntity crawlMarketEntity = CrawlMarketEntity.builder()
                            .crawlMarketSymbol(crawlMarketDto.getSymbol())
                            .crawlMarketRate(crawlMarketDto.getRate())
                            .crawlMarketBefore(crawlMarketDto.getBefore())
                            .crawlMarketCurrent(crawlMarketDto.getCurrent())
                            .crawlMarketName(crawlMarketDto.getName())
                            .build();

                    crawlMarketRepository.save(crawlMarketEntity);
                }
            }
        }
    }
}
