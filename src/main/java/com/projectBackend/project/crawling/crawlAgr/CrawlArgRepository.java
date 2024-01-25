package com.projectBackend.project.crawling.crawlAgr;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CrawlArgRepository extends JpaRepository<CrawlArgEntity, Long> {
    Optional<CrawlArgEntity> findByCrawlExchangeName(String name);
}
