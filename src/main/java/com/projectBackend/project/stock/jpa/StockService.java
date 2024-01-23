package com.projectBackend.project.stock.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@Transactional
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;

    public List<StockEntity> getStockByName(String stockName) {
        return stockRepository.findByStockNameContainingIgnoreCase(stockName);
    }
}
