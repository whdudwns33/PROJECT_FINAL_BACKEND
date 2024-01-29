package com.projectBackend.project.crawling.crawlGold;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CrawlGoldService {
    private final CrawlGoldRepository crawlGoldRepository;

    public List<CrawlGoldDto> getCrawlGoldList() {
        List<CrawlGoldEntity> crawlGoldEntities = crawlGoldRepository.findAll();
        List<CrawlGoldDto> crawlGoldDtoList = new ArrayList<>();
        for (CrawlGoldEntity crawlGoldEntity : crawlGoldEntities) {
            CrawlGoldDto crawlGoldDto = CrawlGoldDto.of(crawlGoldEntity);
            crawlGoldDtoList.add(crawlGoldDto);
        }
        return crawlGoldDtoList;
    }
}
