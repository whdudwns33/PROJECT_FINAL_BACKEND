package com.projectBackend.project.crawling.crawlMetal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CrawlMetalService {
    private final CrawlMetalRepository crawlMetalRepository;

    public List<CrawlMetalDto> getCrawlMetalList() {
        List<CrawlMetalEntity> crawlMetalEntities = crawlMetalRepository.findAll();
        List<CrawlMetalDto> crawlMetalDtoList = new ArrayList<>();
        for (CrawlMetalEntity crawlMetalEntity : crawlMetalEntities) {
            CrawlMetalDto crawlMetalDto = CrawlMetalDto.of(crawlMetalEntity);
            crawlMetalDtoList.add(crawlMetalDto);
        }
        return crawlMetalDtoList;
    }
}
