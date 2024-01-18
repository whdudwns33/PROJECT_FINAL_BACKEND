package com.projectBackend.project.crawling.crawlEnergy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectBackend.project.crawling.crawlExchange.CrawlExchangeDto;
import com.projectBackend.project.crawling.crawlExchange.CrawlExchangeEntity;
import com.projectBackend.project.crawling.crawlStock.CrawlStockRpository;
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
public class CrawlEnergySchedularService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final CrawlEnergyRepository crawlEnergyRepository;

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void performCrawling() throws JsonProcessingException {
        // Flask 애플리케이션의 stockTop 엔드포인트에 POST 요청 보내기
        String url = "http://localhost:5000/python/energy";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String data = response.getBody();

        //assert 문은 주어진 조건이 참이면 계속 진행하고, 거짓이라면 AssertionError를 발생시켜 프로그램을 중단
        assert data != null;
        List<CrawlEnergyDto> dataDtoList = objectMapper.readValue(data, new TypeReference<List<CrawlEnergyDto>>() {});
        // 데이터 최신화를 위해 삭제
        crawlEnergyRepository.deleteAll();
        for (CrawlEnergyDto energyDto : dataDtoList) {
            CrawlEnergyEntity crawlEnergyEntity = CrawlEnergyEntity.builder()
                    .CrawlEnergyName(energyDto.getName())
                    .CrawlEnergyMonth(energyDto.getMonth()) // 예시로 Long으로 변환하였습니다. 필요에 따라 다른 형식으로 변환해주세요.
                    .CrawlEnergyUnits(energyDto.getUnits()) // 마찬가지로 필요에 따라 변환
                    .CrawlEnergyPrice(energyDto.getPrice()) // 필요에 따라 변환
                    .CrawlEnergyYesterday(energyDto.getYesterday()) // 필요에 따라 변환
                    .CrawlEnergyRate(energyDto.getRate()) // 필요에 따라 변환
                    .CrawlEnergyDate(energyDto.getDate()) // 필요에 따라 변환
                    .CrawlEnergyExchange(energyDto.getExchange())
                    .build();

            crawlEnergyRepository.save(crawlEnergyEntity);
        }
    }

}
