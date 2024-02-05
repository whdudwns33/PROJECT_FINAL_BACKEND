package com.projectBackend.project.utils;

import com.projectBackend.project.stock.StockDto;
import com.projectBackend.project.stock.jpa.StockService;
import com.projectBackend.project.utils.websocket.WebSocketHandler;
import com.projectBackend.project.utils.websocket.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonContoroller {
    private final CommonService commonService;
    private final StockService stockService;
    // 웹소켓
    private final WebSocketService webSocketService;

    @GetMapping("/index")
    public ResponseEntity<MultiDto> stockIndex() {
        return ResponseEntity.ok(commonService.getIndex());
    }

    @GetMapping("/list")
    public ResponseEntity<String> stockList (@RequestParam String type) throws ParseException {
        return ResponseEntity.ok(webSocketService.broadcastDtoData("stockList", type));
    }

}