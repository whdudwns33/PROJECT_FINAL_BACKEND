package com.projectBackend.project.crawling.crawlExchange;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CrawlExchangeRepository extends JpaRepository<CrawlExchangeEntity, Long> {
    Optional<CrawlExchangeEntity> findByCrawlExchangeName(String name);
}
