package com.projectBackend.project.crawling.crawlMarket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CrawlMarketService {
    private final CrawlMarketRepository crawlMarketRepository;

    public List<CrawlMarketDto> getCrawlMarketList() {
        List<CrawlMarketEntity> crawlMarketEntities = crawlMarketRepository.findAll();
        List<CrawlMarketDto> crawlMarketDtoList = new ArrayList<>();
        for (CrawlMarketEntity crawlMarketEntity : crawlMarketEntities) {
            CrawlMarketDto crawlMarketDto = CrawlMarketDto.of(crawlMarketEntity);
            crawlMarketDtoList.add(crawlMarketDto);
        }
        return crawlMarketDtoList;
    }
}
