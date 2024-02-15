package com.projectBackend.project.crawling.crawlMajorNews;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CrawlMajorNewsService {
    private final CrawlMajorNewsRepository crawlMajorNewsRepository;

    public List<CrawlMajorNewsDto> getCrawlMajorNewsList() {
        List<CrawlMajorNewsEntity> crawlMajorNewsEntities = crawlMajorNewsRepository.findAll();
        List<CrawlMajorNewsDto> crawlMajorNewsDtos = new ArrayList<>();
        for (CrawlMajorNewsEntity crawlMajorNewsEntity : crawlMajorNewsEntities) {
            CrawlMajorNewsDto crawlMajorNewsDto = CrawlMajorNewsDto.of(crawlMajorNewsEntity);
            crawlMajorNewsDtos.add(crawlMajorNewsDto);
        }
        return crawlMajorNewsDtos;

    }

}
