package com.projectBackend.project.crawling.crawlOil;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CrawlOilRepository extends JpaRepository<CrawlOilEntity, Long> {
    Optional<CrawlOilEntity> findByCrawlOilName(String name);
}
