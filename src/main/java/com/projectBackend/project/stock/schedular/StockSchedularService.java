package com.projectBackend.project.stock.schedular;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectBackend.project.stock.StockDto;
import com.projectBackend.project.stock.jpa.RecentStockEntity;
import com.projectBackend.project.stock.jpa.RecentStockRepository;
import com.projectBackend.project.stock.jpa.StockService;
import com.projectBackend.project.utils.websocket.WebSocketHandler;
import com.projectBackend.project.utils.websocket.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketSession;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockSchedularService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final StockService stockService;
    private final RecentStockRepository recentStockRepository;
    private final WebSocketService webSocketService;
    private final WebSocketHandler webSocketHandler;

//    @Value("${flask.data.path}")
//    private String flaskDataPath;

    @Scheduled(fixedRate = 1000 * 60 * 1)
    public void pullRequest() throws JsonProcessingException {
        // Flask 애플리케이션의 stock 엔드포인트에 새로운 데이터를 달라고 요청 보내기
        String url = "http://localhost:5000/python/stock/pull";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String stockData = response.getBody();

        // 여기에 받은 데이터를 처리하는 로직 추가
        log.info("Stock data received from Flask: {}", stockData);
    }

    @Scheduled(fixedRate = 1000 * 10)
    public void brodcastRequest() {
        for (Map.Entry<String, List<WebSocketSession>> entry : webSocketHandler.getRoomMap().entrySet()) {
            String roomId = entry.getKey();

            // 추가된 조건문
            if ("stockList".equals(roomId)) {
                continue;
            }
            List<WebSocketSession> roomSessions = entry.getValue();
            if (roomSessions != null && !roomSessions.isEmpty()) {
                // 방의 첫 번째 세션을 대표로 사용
                WebSocketSession representativeSession = roomSessions.get(0);
                String name = webSocketHandler.extractRoomId(representativeSession);
                // 현재 날짜를 가져오는 코드
                LocalDate localDate = LocalDate.now();
                // LocalDate를 Date로 변환
                Date currentDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                RecentStockEntity latestStock = recentStockRepository.findLatestByName(name, currentDate);
                if (latestStock != null) {
                    // RecentStockEntity를 Map<String, List<RecentStockEntity>>으로 감싸기
                    Map<String, List<RecentStockEntity>> data = new HashMap<>();
                    data.put("latestStock", Collections.singletonList(latestStock));
                    webSocketService.broadcastData(roomId, data);
                } else {
                    log.warn("주식에 대한 데이터를 찾을 수 없습니다: {}", name);
                }
            }
        }
    }

    // 조영준 : 주식 리스트 조회 세션
    // 임시 10초
    // 1시간 정도로 생각
    @Scheduled(fixedRate = 1000 * 10)
    public void brodcastRequestStockList() throws ParseException {
        for (Map.Entry<String, List<WebSocketSession>> entry : webSocketHandler.getRoomMap().entrySet()) {
            String roomId = entry.getKey();
            // 추가된 조건문
            if ("stockList".equals(roomId)) {
                List<WebSocketSession> roomSessions = entry.getValue();
                if (roomSessions != null && !roomSessions.isEmpty()) {
                    // 방의 첫 번째 세션을 대표로 사용
                    WebSocketSession representativeSession = roomSessions.get(0);
                    String type = webSocketHandler.extractType(representativeSession);
                    // 매개변수
                    log.info("type : {}", type);
                    log.info("roomSessions의 수 : {}", roomSessions.size());
                    List<StockDto> stockDtoList = stockService.getStockList(type);
                    if (stockDtoList != null) {
                        webSocketService.broadcastDtoData(roomId, stockDtoList);
                    } else {
                        log.warn("주식 리스트에 대한 데이터를 찾을 수 없습니다: {}", type);
                    }
                }
            }
        }
    }
}
