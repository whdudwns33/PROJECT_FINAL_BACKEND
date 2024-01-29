package com.projectBackend.project.crawling.crawlOil;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CrawlOilService {
    private final CrawlOilRepository crawlOilRepository;

    public List<CrawlOilDto> getCrawlOilList() {
        List<CrawlOilEntity> crawlOilEntities = crawlOilRepository.findAll();
        List<CrawlOilDto> crawlOilDtoList = new ArrayList<>();
        for (CrawlOilEntity crawlOilEntity : crawlOilEntities) {
            CrawlOilDto crawlOilDto = CrawlOilDto.of(crawlOilEntity);
            crawlOilDtoList.add(crawlOilDto);
        }
        return crawlOilDtoList;
    }
}
