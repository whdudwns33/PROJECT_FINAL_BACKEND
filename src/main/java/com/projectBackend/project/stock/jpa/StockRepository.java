package com.projectBackend.project.stock.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<StockEntity, Long> {
    List<StockEntity> findByStockNameContainingIgnoreCase(String stockName);

}
