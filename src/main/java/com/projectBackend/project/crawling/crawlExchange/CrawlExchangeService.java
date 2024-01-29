package com.projectBackend.project.crawling.crawlExchange;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CrawlExchangeService {
    private final CrawlExchangeRepository crawlExchangeRepository;

    public List<CrawlExchangeDto> getCrawlExchangeList() {
        List<CrawlExchangeEntity> crawlExchangeEntities = crawlExchangeRepository.findAll();
        List<CrawlExchangeDto> crawlExchangeDtoList = new ArrayList<>();
        for (CrawlExchangeEntity crawlExchangeEntity : crawlExchangeEntities) {
            CrawlExchangeDto crawlExchangeDto = CrawlExchangeDto.of(crawlExchangeEntity);
            crawlExchangeDtoList.add(crawlExchangeDto);
        }
        return crawlExchangeDtoList;
    }
}
