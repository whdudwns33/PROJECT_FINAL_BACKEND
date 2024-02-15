package com.projectBackend.project.crawling.crawlRate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CrawlRateService {
    private final CrawlRateRepository crawlRateRepository;

    public List<CrawlRateDto> getCrawlRateList() {
        List<CrawlRateEntity> crawlRateEntities = crawlRateRepository.findAll();
        List<CrawlRateDto> crawlRateDtoList = new ArrayList<>();
        for (CrawlRateEntity crawlRateEntity : crawlRateEntities) {
            CrawlRateDto crawlRateDto = CrawlRateDto.of(crawlRateEntity);
            crawlRateDtoList.add(crawlRateDto);
        }
        return crawlRateDtoList;
    }
}
