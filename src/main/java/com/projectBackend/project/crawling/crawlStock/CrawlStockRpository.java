package com.projectBackend.project.crawling.crawlStock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrawlStockRpository extends JpaRepository<CrawlStockEntity, Long> {


}
