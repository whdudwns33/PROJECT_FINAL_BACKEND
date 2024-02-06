//package com.projectBackend.project.stock.jpa.listner;
//
//import com.projectBackend.project.stock.jpa.RecentStockEntity;
//import com.projectBackend.project.stock.jpa.RecentStockRepository;
//import com.projectBackend.project.utils.websocket.WebSocketHandler;
//import com.projectBackend.project.utils.websocket.WebSocketService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.PostUpdate;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.util.*;
//
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class RecentStockEntityListener {
//    private final WebSocketHandler webSocketHandler;
//    private final WebSocketService webSocketService;
//    private final RecentStockRepository recentStockRepository;
//    @PostUpdate
//    @Transactional
//    public void handlePostUpdate(Object o) {
//        log.info("handleStockDataChange 동작");
//        // recent_stock 테이블에 업데이트 이후 실행되는 코드
//
//        // 각 roomId에 대해 브로드캐스트 메시지를 보내는 로직 추가
//        for (String roomId : webSocketHandler.getRoomMap().keySet()) {
//            LocalDate localDate = LocalDate.now();
//            Date currentDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
//
//            RecentStockEntity latestStock = recentStockRepository.findLatestByName(roomId, currentDate);
//
//            if (latestStock != null) {
//                // RecentStockEntity를 Map<String, List<RecentStockEntity>>으로 감싸기
//                Map<String, List<RecentStockEntity>> data = new HashMap<>();
//                data.put("latestStock", Collections.singletonList(latestStock));
//
//                // 해당 roomId에 매핑된 세션들에게 데이터 전송
//                webSocketService.broadcastData(roomId, data);
//            } else {
//                log.warn("주식에 대한 데이터를 찾을 수 없습니다: {}", roomId);
//            }
//        }
//    }
//}
