package com.projectBackend.project.crawling.crawlStock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CrawlStockService {
    private final CrawlStockRepository crawlStockRepository;

    public List<CrawlStockDto> getCrawlStockList() {
        List<CrawlStockEntity> crawlStockEntities = crawlStockRepository.findAll();
        List<CrawlStockDto> crawlStockDtoList = new ArrayList<>();
        for (CrawlStockEntity crawlStockEntity : crawlStockEntities) {
            CrawlStockDto crawlStockDto = CrawlStockDto.of(crawlStockEntity);
            crawlStockDtoList.add(crawlStockDto);
        }
        return crawlStockDtoList;
    }

    // You can add more methods here for specific business logic related to stock data.
    // For example, methods to get stock by ID, save new stock data, update stock data, etc.
}
