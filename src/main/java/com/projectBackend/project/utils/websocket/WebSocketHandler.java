package com.projectBackend.project.utils.websocket;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Getter
public class WebSocketHandler extends TextWebSocketHandler {

    // Getter for roomMap
    // 방과 세션을 관리하는 Map
    private final Map<String, List<WebSocketSession>> roomMap = new HashMap<>();
    // 다중 room과 세션 관리
    private final Map<String, List<WebSocketSession>> roomMaps = new HashMap<>();
    // 세션과 방 ID를 매핑하는 Map
    private final Map<WebSocketSession, String> sessionRoomIdMap = new HashMap<>();
    // 세션 생성 카운트
    int sessionCount = 0;
    // close 카운트
    int closeCount = 0;


    // 세션이 연결되면 동작할 메서드
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebSocket 연결이 성립되었습니다. 세션 ID: {}", session.getId());
        log.info("세션이 연결된 횟수: {}", sessionCount);
        sessionCount++;
        // 최초 연결 시, room을 생성하고 세션을 등록
        createRoomAndAddSession(session);
    }

    // 세션이 연결 해제 되면 동작할 메서드
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomId = sessionRoomIdMap.get(session);
        log.info("세션이 해제된 수 : {}", closeCount);
        if (roomId != null) {
            removeSessionFromRoom(roomId, session);

            // Room에 등록된 세션이 없으면 Room을 제거
            if (roomMap.get(roomId).isEmpty()) {
                roomMap.remove(roomId);
            }
        }
        closeCount++;
    }

    // 메시지 수신 시 동작할 메서드
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Handle incoming messages
        log.info("Received message: {}", message.getPayload());
    }

    // 최초 연결 시, room을 생성하고 세션을 등록하는 메서드
    private void createRoomAndAddSession(WebSocketSession session) {
        // roomId 추출
        String roomId = extractRoomId(session);

        // roomId가 없다면 기본값으로 설정
        if (roomId == null || roomId.isEmpty()) {
            log.warn("Received WebSocket connection without roomId. Using default roomId.");
            return; // roomId가 없으면 아무 동작도 하지 않고 종료
        }

        createRoom(roomId);
        addSessionToRoom(roomId, session);

    }

    // 최초 연결 시, room을 생성하고 세션을 등록하는 메서드
    private void createRoom(String roomId) {
        roomMap.put(roomId, new ArrayList<>());
    }

    // 룸에 세션 등록
    private void addSessionToRoom(String roomId, WebSocketSession session) {
        roomMap.computeIfAbsent(roomId, key -> new ArrayList<>()).add(session);
        sessionRoomIdMap.put(session, roomId);
    }

    // 룸에서 세션 삭제
    private void removeSessionFromRoom(String roomId, WebSocketSession session) {
        List<WebSocketSession> roomSessions = roomMap.get(roomId);
        if (roomSessions != null) {
            roomSessions.remove(session);
        }
        sessionRoomIdMap.remove(session);
    }

    // 세션에서 roomId 추출
    public String extractRoomId(WebSocketSession session) {
        String query = session.getUri().getQuery();
        if (query != null) {
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("room=")) {
                    return param.substring("room=".length());
                }
            }
        }
        return null;
    }


    // 세션에서 type 추출
    public String extractType(WebSocketSession session) {
        String query = session.getUri().getQuery();
        if (query != null) {
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("type=")) {
                    return param.substring("type=".length());
                }
            }
        }
        return null;
    }

}
