package com.projectBackend.project.utils.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectBackend.project.stock.StockDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class WebSocketService {

    @Autowired
    private WebSocketHandler webSocketHandler;

    // 제네릭 < T > 알아보기
    public void broadcastStockData(String roomId, Map<String, List<StockDto>> stockDataMap) {
        // Convert stockDataMap to a JSON string
        String messageJson = convertMapToJson(stockDataMap);

        WebSocketMessageDto messageDto = WebSocketMessageDto.builder()
                .type("STOCK_DATA")
                .message(messageJson)
                .build();

        Map<String, List<WebSocketSession>> roomMap = webSocketHandler.getRoomMap();
        List<WebSocketSession> roomSessions = roomMap.get(roomId);

        if (roomSessions != null) {
            for (WebSocketSession session : roomSessions) {
                sendMessage(session, convertMessageDtoToJson(messageDto));
            }
        }
    }

    private void sendMessage(WebSocketSession session, String message) {
        try {
            session.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., log it) based on your application's requirements
        }
    }

    private String convertMessageDtoToJson(WebSocketMessageDto messageDto) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(messageDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // Handle the exception based on your application's requirements
            return ""; // Return an empty string or handle it accordingly
        }
    }

    private String convertMapToJson(Map<String, List<StockDto>> dataMap) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(dataMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // Handle the exception based on your application's requirements
            return ""; // Return an empty string or handle it accordingly
        }
    }
}