package com.projectBackend.project.crawling.crawlSearch;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CrawlSearchRepository extends JpaRepository<CrawlSearchEntity, Long> {

    Optional<CrawlSearchEntity> findBySearchName(String searchName);
}
