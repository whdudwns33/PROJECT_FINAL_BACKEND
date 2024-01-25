package com.projectBackend.project.crawling.crawlMarket;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CrawlMarketRepository extends JpaRepository<CrawlMarketEntity, Long> {
    Optional<CrawlMarketEntity> findByCrawlMarketSymbol(String symbol);
}
