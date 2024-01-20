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
    private final StockJdbcBulkInsert stockJdbcBulkInsert;
    @PostMapping("/data")
    public ResponseEntity<String> loadStock(@RequestBody List<StockDto> stockDtoList) {
        stockJdbcBulkInsert.bulkInsert(stockDtoList);
        log.info("Stock Data received successfully");
        return ResponseEntity.ok("Stock Data received successfully");
    }

    @GetMapping("/get/{stockName}")
    public ResponseEntity<List<StockEntity>> getStockByName(@PathVariable String stockName) {
        List<StockEntity> stocks = stockService.getStockByName(stockName);

        if (stocks.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(stocks);
        }
    }
}