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
public class CrawlDomesticIndicatorsSchedularService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final CrawlDomesticIndicatorsRepository crawlGoldRepository;

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void performCrawling() throws JsonProcessingException {
        // Flask 애플리케이션의 domesticindicators 엔드포인트에 POST 요청 보내기
        String url = "http://localhost:5000/python/domesticindicators";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String data = response.getBody();

        //assert 문은 주어진 조건이 참이면 계속 진행하고, 거짓이라면 AssertionError를 발생시켜 프로그램을 중단
        assert data != null;
        List<CrawlDomesticIndicatorsDto> dataDtoList = objectMapper.readValue(data, new TypeReference<List<CrawlDomesticIndicatorsDto>>() {});

        // 데이터 최신화를 위해 삭제
        crawlGoldRepository.deleteAll();

        for (CrawlDomesticIndicatorsDto domesticIndicatorsDtoDto : dataDtoList) {
            CrawlDomesticIndicatorsEntity crawlDomesticIndicatorsEntity = CrawlDomesticIndicatorsEntity.builder()
                    .CrawlDomesticIndicatorsName(domesticIndicatorsDtoDto.getName())
                    .CrawlDomesticIndicatorsPrice(domesticIndicatorsDtoDto.getPrice())
                    .CrawlDomesticIndicatorsChange(domesticIndicatorsDtoDto.getChangepoint())
                    .build();

            crawlGoldRepository.save(crawlDomesticIndicatorsEntity);
        }
    }
}
