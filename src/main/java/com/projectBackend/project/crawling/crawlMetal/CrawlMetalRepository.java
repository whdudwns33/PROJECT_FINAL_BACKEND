package com.projectBackend.project.crawling.crawlMetal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CrawlMetalRepository extends JpaRepository<CrawlMetalEntity, Long> {
    Optional<CrawlMetalEntity> findByCrawlMetalName(String name);
}
