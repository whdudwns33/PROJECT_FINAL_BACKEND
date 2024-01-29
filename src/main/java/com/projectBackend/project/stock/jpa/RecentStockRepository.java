package com.projectBackend.project.stock.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecentStockRepository extends JpaRepository<RecentStockEntity, Long> {
    List<StockEntity> findByStockNameContainingIgnoreCase(String stockName);
    boolean existsByStockDateStartingWith(String yearMonth);
}
