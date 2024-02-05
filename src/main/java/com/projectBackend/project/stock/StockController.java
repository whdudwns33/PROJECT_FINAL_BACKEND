package com.projectBackend.project.stock;

import com.projectBackend.project.stock.elastic.StockElasticService;
import com.projectBackend.project.stock.jdbc.StockJdbcBatchService;
import com.projectBackend.project.stock.jpa.StockEntity;
import com.projectBackend.project.stock.jpa.StockService;
import com.projectBackend.project.utils.websocket.WebSocketHandler;
import com.projectBackend.project.utils.websocket.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;
    private final StockJdbcBatchService stockJdbcBatchService;
    private final StockElasticService stockElasticService;
    @PostMapping("/data")
    public ResponseEntity<String> loadStock(@RequestBody Map<String, List<StockDto>> stockDataMap) {

//        System.out.println(stockDataMap.values());
        // elasticSearch에 저장 코드 추가
        log.info("Stock Data Elastic load");
        stockElasticService.saveStocksToElasticsearch(stockDataMap);

        log.info("Stock Data Process");
        stockService.batchInsertOrUpdate(stockDataMap);


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

//    @GetMapping("/getStock")
//    public ResponseEntity<StockDto> getStockInfo(@RequestParam String name) {
//        ResponseEntity.ok()
//    }
}