package com.projectBackend.project.crawling.crawlOverseasIndicators;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CrawlOverseasIndicatorsService {
    private final CrawlOverseasIndicatorsRepository crawlOverseasIndicatorsRepository;

    public List<CrawlOverseasIndicatorsDto> getCrawlOverseasIndicatorsList() {
        List<CrawlOverseasIndicatorsEntity> crawlOverseasIndicatorsEntities = crawlOverseasIndicatorsRepository.findAll();
        List<CrawlOverseasIndicatorsDto> crawlOverseasIndicatorsDtoList = new ArrayList<>();
        for (CrawlOverseasIndicatorsEntity crawlOverseasIndicatorsEntity : crawlOverseasIndicatorsEntities) {
            CrawlOverseasIndicatorsDto crawlOverseasIndicatorsDto = CrawlOverseasIndicatorsDto.of(crawlOverseasIndicatorsEntity);
            crawlOverseasIndicatorsDtoList.add(crawlOverseasIndicatorsDto);
        }
        return crawlOverseasIndicatorsDtoList;
    }
}
