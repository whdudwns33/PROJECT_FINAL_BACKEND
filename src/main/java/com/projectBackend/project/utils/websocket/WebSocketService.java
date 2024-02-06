package com.projectBackend.project.utils.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectBackend.project.stock.StockDto;
import com.projectBackend.project.stock.jpa.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Service
public class WebSocketService {

    @Autowired
    private WebSocketHandler webSocketHandler;

    @Autowired
    private StockService stockService;

    // Use generics for the broadcastData method
    public <T> void broadcastData(String roomId, Map<String, List<T>> dataMap) {
        // Convert dataMap to a JSON string
        String messageJson = convertMapToJson(dataMap);

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

    // 주식 리스트 조회
    private int num = 0;
    public String broadcastDtoData(String roomId, List<StockDto> stockDtos) throws ParseException {
        // Dto리스트를 제이슨 문자열화
        String messageJson = convertDtoToJson(stockDtos);

        WebSocketMessageDto messageDto = WebSocketMessageDto.builder()
                .type(String.valueOf(num))
                .message(messageJson)
                .build();

        Map<String, List<WebSocketSession>> roomMap = webSocketHandler.getRoomMap();
        List<WebSocketSession> roomSessions = roomMap.get(roomId);

        if (roomSessions != null) {
            for (WebSocketSession session : roomSessions) {
                sendMessage(session, convertMessageDtoToJson(messageDto));
                num ++;
            }
        }
        return convertMessageDtoToJson(messageDto);
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

    private <T> String convertMapToJson(Map<String, List<T>> dataMap) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(dataMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // Handle the exception based on your application's requirements
            return ""; // Return an empty string or handle it accordingly
        }
    }

    // 조영준 : 맵 대신 Dto로 받는 로직
    private String convertDtoToJson(Object dto) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
}