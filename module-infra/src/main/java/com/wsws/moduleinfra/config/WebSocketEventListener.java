package com.wsws.moduleinfra.config;

import com.wsws.modulecommon.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketEventListener {

    private final RedisService redisService;

    // WebSocket 연결 성공 시 호출되는 메서드 (세션 초기화 및 사용자 연결 확인)
    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        System.out.println("STOMP 연결 성공!!");
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String sessionId = headerAccessor.getSessionId();
        log.info("WebSocket 연결 성공! 세션 ID: {}", sessionId);
    }

    // WebSocket 연결 성공 시 호출되는 메서드
    @EventListener
    public void handleWebSocketSubscribe(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String userId = getUserId(headerAccessor);
        String roomId = getRoomId(headerAccessor);
        String sessionId = headerAccessor.getSessionId();

        log.info("roomId = {}", roomId);
        log.info("userId = {}", userId);

        // Redis에 세션 ID와 userId 저장
        redisService.saveSessionUserMapping(sessionId, userId);

        redisService.saveUserRoomMapping(userId, roomId);
        log.info("사용자 {}가 채팅방 {}에 입장했습니다. Redis에 기록했습니다.", userId, roomId);
    }

    // WebSocket 연결 끊어졌을 때 호출되는 메서드
    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String sessionId = headerAccessor.getSessionId();
        String userId = redisService.getUserBySessionId(sessionId);

        if(userId != null) {
            redisService.removeUserRoomMapping(userId); // 채팅방 매핑 제거
            redisService.removeSessionUserMapping(sessionId); // 세션 ID 매핑 제거
            log.info("사용자 {}의 채팅방 정보가 Redis에서 삭제되었습니다.", userId);
        } else {
            log.error("STOMP 연결 해제 시 userId를 찾을 수 없습니다.");
        }

        log.info("STOMP 연결 해제"+userId);
    }

    private String getUserId(StompHeaderAccessor headerAccessor) {
        String userId = headerAccessor.getFirstNativeHeader("senderId");

        if(userId == null) {
            throw new NullPointerException("WebSocket 요청에서 userId 헤더를 찾을 수 없습니다.");
        }

        return userId;
    }

    private String getRoomId(StompHeaderAccessor headerAccessor) {
        String destination = headerAccessor.getDestination();
        if (destination == null || !destination.startsWith("/sub/chat/")) {
            log.error("잘못된 요청입니다. destination: {}", destination);
            throw new IllegalArgumentException("WebSocket 요청에서 유효하지 않은 roomId를 받았습니다: " + destination);
        }

        String roomId = destination.split("/sub/chat/")[1];
        if (roomId == null || roomId.isEmpty()) {
            log.error("roomId가 비어 있습니다. destination: {}", destination);
            throw new IllegalArgumentException("WebSocket 요청에서 유효하지 않은 roomId를 받았습니다: " + destination);
        }

        return roomId;
    }
}
