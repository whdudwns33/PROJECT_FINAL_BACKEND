package com.projectBackend.project.crawling.crawlSearch;


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
public class CrawlSearchSchedularService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final CrawlSearchRepository crawlSearchRepository;

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void performCrawling() throws JsonProcessingException {
        // Modify the URL to the appropriate endpoint for search crawling
        String url = "http://localhost:5000/python/search";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String data = response.getBody();

        if (data != null) {
            List<CrawlSearchDto> dataDtoList = objectMapper.readValue(data, new TypeReference<List<CrawlSearchDto>>() {});

            // Data refresh for the latest information
            crawlSearchRepository.deleteAll();

            for (CrawlSearchDto searchDto : dataDtoList) {
                // Search by name for existing data
                Optional<CrawlSearchEntity> existingEntityOptional = crawlSearchRepository.findBySearchName(searchDto.getSearchName());

                if (existingEntityOptional.isPresent()) {
                    // If existing data is found, update it
                    CrawlSearchEntity crawlSearchEntity = existingEntityOptional.get();
                    crawlSearchEntity.setSearchCount(searchDto.getSearchCount());
                    crawlSearchEntity.setSearchUpdown(searchDto.getSearchUpdown());
                    crawlSearchEntity.setSearchPrice(searchDto.getSearchPrice());
                    crawlSearchEntity.setSearchChangeRate(searchDto.getSearchChangeRate());
                    crawlSearchEntity.setSearchMarketCap(searchDto.getSearchMarketCap());

                    crawlSearchRepository.save(crawlSearchEntity);
                } else {
                    // If no existing data is found, add new data
                    CrawlSearchEntity crawlSearchEntity = CrawlSearchEntity.builder()
                            .searchName(searchDto.getSearchName())
                            .searchCount(searchDto.getSearchCount())
                            .searchUpdown(searchDto.getSearchUpdown())
                            .searchPrice(searchDto.getSearchPrice())
                            .searchChangeRate(searchDto.getSearchChangeRate())
                            .searchMarketCap(searchDto.getSearchMarketCap())
                            .build();

                    crawlSearchRepository.save(crawlSearchEntity);
                }
            }
        }
    }
}
