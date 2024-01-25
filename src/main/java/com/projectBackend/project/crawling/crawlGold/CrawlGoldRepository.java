package com.projectBackend.project.crawling.crawlGold;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CrawlGoldRepository extends JpaRepository<CrawlGoldEntity, Long> {
    Optional<CrawlGoldEntity> findByCrawlGoldName(String name);
}
