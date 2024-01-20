package com.projectBackend.project.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
//@Transactional
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;

    public List<StockEntity> getStockByName(String stockName) {
        return stockRepository.findByStockNameContainingIgnoreCase(stockName);
    }
}
