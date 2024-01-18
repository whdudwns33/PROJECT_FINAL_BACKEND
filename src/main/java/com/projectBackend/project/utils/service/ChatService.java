//package com.projectBackend.project.utils.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.projectBackend.project.dto.ChatRoomResDTO;
//import com.projectBackend.project.entity.Chat;
//import com.projectBackend.project.entity.ChatRoom;
//import com.projectBackend.project.entity.Member;
//import com.projectBackend.project.repository.ChatRepository;
//import com.projectBackend.project.repository.ChatRoomRepository;
//import com.projectBackend.project.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//
//import javax.annotation.PostConstruct;
//import java.time.LocalDateTime;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class ChatService {
//    private final ObjectMapper objectMapper;
//    private Map<String, ChatRoomResDTO> chatRooms;
//    private final ChatRoomRepository chatRoomRepository;
//    private final ChatRepository chatRepository;
//    private final UserRepository userRepository;
//    Map<String, List<WebSocketSession>> roomSessions = new HashMap<>();
//
//
////    @PostConstruct // 의존성 주입 이후 초기화를 수행
////    private void init(){
////        chatRooms = new LinkedHashMap<>();
////    }
//@PostConstruct
//private void init(){
//    chatRooms = new LinkedHashMap<>();
//    List<ChatRoom> chatRoomEntityList = chatRoomRepository.findAll();
//    for (ChatRoom chatRoomEntity : chatRoomEntityList) {
//        ChatRoomResDTO chatRoom = ChatRoomResDTO.builder()
//                .roomId(chatRoomEntity.getRoomId())
//                .name(chatRoomEntity.getRoomName())
//                .regDate(chatRoomEntity.getCreatedAt())
//                .build();
//        chatRooms.put(chatRoomEntity.getRoomId(), chatRoom);
//    }
//}
//    public List<ChatRoomResDTO> findAllRoom() {
//        return new ArrayList<>(chatRooms.values());
//    }
//    // ownerId로 채팅방 목록 가져오기
//    public List<ChatRoomResDTO> findRoomsByOwnerId(Long ownerId) {
//        List<ChatRoom> chatRooms = chatRoomRepository.findByOwnerId(ownerId);
//        return chatRooms.stream()
//                .map(chatRoom -> new ChatRoomResDTO(chatRoom.getRoomId(), chatRoom.getRoomName(), chatRoom.getCreatedAt(), chatRoom.getOwner().getUserEmail()))
//                .collect(Collectors.toList());
//    }
//
//    public ChatRoomResDTO findRoomById(String roomId) {
//        return chatRooms.get(roomId);
//    }
//    // 방 개설하기
//    public ChatRoomResDTO createRoom(String name,  String ownerEmail) {
//        String randomId = UUID.randomUUID().toString();
//        log.info("UUID : " + randomId);
//        Member owner = userRepository.findByUserEmail(ownerEmail)
//                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
//
//        ChatRoomResDTO chatRoom = ChatRoomResDTO.builder()
//                .roomId(randomId)
//                .name(name)
//                .regDate(LocalDateTime.now())
//                .ownerEmail(ownerEmail) // 소유자 이메일 설정
//                .build();
//
//        ChatRoom chatRoomEntity = new ChatRoom();
//        chatRoomEntity.setRoomId(randomId);
//        chatRoomEntity.setOwner(owner); // 채팅방 소유자 설정
//        chatRoomEntity.setRoomName(name);
//        chatRoomEntity.setCreatedAt(LocalDateTime.now());
//        chatRoomRepository.save(chatRoomEntity);
//        chatRooms.put(randomId, chatRoom);
//        return chatRoom;
//    }
//    public void removeRoom(String roomId) {
//        ChatRoomResDTO room = chatRooms.get(roomId);
//        if (room != null) {
//            if (room.isSessionEmpty()) {
//                chatRooms.remove(roomId);
//                ChatRoom chatRoomEntity = chatRoomRepository.findById(roomId).orElseThrow(
//                        () -> new RuntimeException("해당 채팅방이 존재하지 않습니다.")
//                );
//                if (chatRoomEntity != null) {
//                    chatRoomRepository.delete(chatRoomEntity);
//                }
//            }
//        }
//    }
//    public <T> void sendMessage(WebSocketSession session, T message){
//        try {
//            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
//        } catch (Exception e){
//            log.error(e.getMessage(), e);
//        }
//    }
//    public void saveMessage(String roomId, String sender, String message) {
//        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
//                .orElseThrow(() -> new RuntimeException("해당 채팅방이 존재하지 않습니다."));
//        Chat chatMessage = new Chat();
//        chatMessage.setChatRoom(chatRoom);
//        chatMessage.setSender(sender);
//        chatMessage.setMessage(message);
//        chatMessage.setSentAt(LocalDateTime.now());
//        chatRepository.save(chatMessage);
//    }
//    // 세션 수 가져오기
//    public int getSessionCount(String roomId) {
//        List<WebSocketSession> sessions = roomSessions.get(roomId);
//        return sessions != null ? sessions.size() : 0;
//    }
//    // 이전 채팅 가져오기
//    public List<Chat> getRecentMessages(String roomId) {
//        return chatRepository.findRecentMessages(roomId);
//    }
//}
