package com.projectBackend.project.stock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;
    @PostMapping("/data")
    public ResponseEntity<String> loadStock(@RequestBody List<StockDto> stockDtoList) {
        List<StockEntity> stockEntityList = stockService.convertToEntities(stockDtoList);
        stockService.saveStocks(stockEntityList);
        log.info("Stock Data received successfully");
        return ResponseEntity.ok("Stock Data received successfully");
    }
}