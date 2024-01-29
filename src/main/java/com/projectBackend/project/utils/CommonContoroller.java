package com.projectBackend.project.utils;

import com.projectBackend.project.stock.StockDto;
import com.projectBackend.project.stock.jpa.RecentStockEntity;
import com.projectBackend.project.stock.jpa.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonContoroller {
    private final CommonService commonService;
    private final StockService stockService;

    @GetMapping("/index")
    public ResponseEntity<MultiDto> stockIndex() {
        return ResponseEntity.ok(commonService.getIndex());
    }

    @GetMapping("/list")
    public ResponseEntity<List<StockDto>> stockList (@RequestParam String type) {
        return ResponseEntity.ok(stockService.getStockList(type));
    }

}
