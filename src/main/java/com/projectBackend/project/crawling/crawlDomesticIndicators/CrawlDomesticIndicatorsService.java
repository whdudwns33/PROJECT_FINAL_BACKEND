package com.projectBackend.project.crawling.crawlDomesticIndicators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrawlDomesticIndicatorsService {
    private final CrawlDomesticIndicatorsRepository crawlDomesticIndicatorsRepository;

    public List<CrawlDomesticIndicatorsDto> getCrawlDomesticIndicatorsList() {
        List<CrawlDomesticIndicatorsEntity> crawlDomesticIndicatorsEntities = crawlDomesticIndicatorsRepository.findAll();
        // Stream을 사용하여 각 Entity를 DTO로 변환하고, 그 결과를 리스트로 수집하여 반환
        return crawlDomesticIndicatorsEntities.stream()
                .map(CrawlDomesticIndicatorsDto::of)
                .collect(Collectors.toList());
    }
}
