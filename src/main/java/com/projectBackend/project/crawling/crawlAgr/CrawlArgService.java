package com.projectBackend.project.crawling.crawlAgr;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CrawlArgService {
    private final CrawlArgRepository crawlArgRepository;

    public List<CrawlArgDto> getCrawlArgs () {
        List<CrawlArgEntity> crawlArgEntities = crawlArgRepository.findAll();
        List<CrawlArgDto> crawlArgDtos = new ArrayList<>();
        for (CrawlArgEntity crawlArgEntity : crawlArgEntities) {
            CrawlArgDto crawlArgDto = CrawlArgDto.of(crawlArgEntity);
            crawlArgDtos.add(crawlArgDto);
        }
        return crawlArgDtos;
    }
}
