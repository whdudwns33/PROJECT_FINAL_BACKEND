package com.projectBackend.project.crawling.crawlStock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrawlStockRepository extends JpaRepository<CrawlStockEntity, Long> {


}
