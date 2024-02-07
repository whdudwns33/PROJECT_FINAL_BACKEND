package com.projectBackend.project.crawling.crawlDomesticIndicators;


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
public class CrawlDomesticIndicatorsSchedulerService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final CrawlDomesticIndicatorsRepository crawlDomesticIndicatorsRepository;

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void performCrawling() throws JsonProcessingException {
        // Flask 애플리케이션의 stockTop 엔드포인트에 get 요청 보내기
        String url = "http://localhost:5000/python/domesticIndicators";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String data = response.getBody();

        if (data != null) {
            List<CrawlDomesticIndicatorsDto> dataDtoList = objectMapper.readValue(data, new TypeReference<List<CrawlDomesticIndicatorsDto>>() {});

            // Data refresh for the latest information
            crawlDomesticIndicatorsRepository.deleteAll();

            for (CrawlDomesticIndicatorsDto domesticIndicatorsDto : dataDtoList) {
                CrawlDomesticIndicatorsEntity crawlDomesticIndicatorsEntity = CrawlDomesticIndicatorsEntity.builder()
                        .crawlDomesticIndicatorsName(domesticIndicatorsDto.getCrawlDomesticIndicatorsName())
                        .crawlDomesticIndicatorsPrice(domesticIndicatorsDto.getCrawlDomesticIndicatorsPrice())
                        .crawlDomesticIndicatorsChange(domesticIndicatorsDto.getCrawlDomesticIndicatorsChange())
                        .build();

                crawlDomesticIndicatorsRepository.save(crawlDomesticIndicatorsEntity);
            }
        }
    }
}
