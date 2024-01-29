package com.projectBackend.project.crawling.crawlEnergy;

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

        if (data != null) {
            List<CrawlEnergyDto> dataDtoList = objectMapper.readValue(data, new TypeReference<List<CrawlEnergyDto>>() {
            });

            for (CrawlEnergyDto energyDto : dataDtoList) {
                // 해당 이름으로 데이터 존재하면 업데이트, 없으면 추가
                Optional<CrawlEnergyEntity> existingEntityOptional = crawlEnergyRepository.findByCrawlEnergyName(energyDto.getName());
                if (existingEntityOptional.isPresent()) {
                    CrawlEnergyEntity existingEntity = existingEntityOptional.get();
                    existingEntity.setCrawlEnergyDate(energyDto.getDate());
                    existingEntity.setCrawlEnergyExchange(energyDto.getExchange());
                    existingEntity.setCrawlEnergyMonth(energyDto.getMonth());
//                    existingEntity.setCrawlEnergyYesterday(energyDto.getYesterday());
                    existingEntity.setCrawlEnergyName(energyDto.getName());
                    existingEntity.setCrawlEnergyUnits(energyDto.getUnits());
                    existingEntity.setCrawlEnergyRate(energyDto.getRate());
                    existingEntity.setCrawlEnergyPrice(energyDto.getPrice());

                    crawlEnergyRepository.saveAndFlush(existingEntity);
                }
                else {
                    CrawlEnergyEntity crawlEnergyEntity = CrawlEnergyEntity.builder()
                            .crawlEnergyName(energyDto.getName())
                            .crawlEnergyMonth(energyDto.getMonth()) // 예시로 Long으로 변환하였습니다. 필요에 따라 다른 형식으로 변환해주세요.
                            .crawlEnergyUnits(energyDto.getUnits()) // 마찬가지로 필요에 따라 변환
                            .crawlEnergyPrice(energyDto.getPrice()) // 필요에 따라 변환
//                            .crawlEnergyYesterday(energyDto.getYesterday()) // 필요에 따라 변환
                            .crawlEnergyRate(energyDto.getRate()) // 필요에 따라 변환
                            .crawlEnergyDate(energyDto.getDate()) // 필요에 따라 변환
                            .crawlEnergyExchange(energyDto.getExchange())
                            .build();

                    crawlEnergyRepository.save(crawlEnergyEntity);
                }
            }
        }
    }
}
