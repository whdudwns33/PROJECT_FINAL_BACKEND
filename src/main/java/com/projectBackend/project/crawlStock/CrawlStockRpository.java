package com.projectBackend.project.crawlStock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrawlStockRpository extends JpaRepository<CrawlStockEntity, Long> {


}
