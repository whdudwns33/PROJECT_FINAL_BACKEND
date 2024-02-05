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
public class WebSocketHandler extends TextWebSocketHandler {

    // Getter for roomMap
    // 방과 세션을 관리하는 Map
    @Getter
    private final Map<String, List<WebSocketSession>> roomMap = new HashMap<>();

    // 세션과 방 ID를 매핑하는 Map
    private final Map<WebSocketSession, String> sessionRoomIdMap = new HashMap<>();

    // 세션이 연결되면 동작할 매서드
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebSocket 연결이 성립되었습니다. 세션 ID: {}", session.getId());

        // 최초 연결 시, room을 생성하고 세션을 등록
        createRoomAndAddSession(session);
    }

    // 세션이 연결 해제 되면 동작할 매서드
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomId = sessionRoomIdMap.get(session);
        if (roomId != null) {
            removeSessionFromRoom(roomId, session);
        }
    }

    // 최초 연결 시, room을 생성하고 세션을 등록하는 메서드
    private void createRoomAndAddSession(WebSocketSession session) {
        // 해당 세션의 roomId 가져오기
        String roomId = sessionRoomIdMap.get(session);

        // roomId가 없다면 생성
        if (roomId == null) {
            roomId = "stock";  // 기본적으로 "stock"로 설정
            createRoom(roomId);
            addSessionToRoom(roomId, session);
        } else {
            // roomId가 이미 존재하면 해당 roomId의 room에 세션을 추가
            addSessionToRoom(roomId, session);
        }
    }

    // 룸 생성
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
            // Room에 등록된 세션이 없으면 Room을 제거
            if (roomSessions.isEmpty()) {
                roomMap.remove(roomId);
            }
        }
        sessionRoomIdMap.remove(session);
    }

}
