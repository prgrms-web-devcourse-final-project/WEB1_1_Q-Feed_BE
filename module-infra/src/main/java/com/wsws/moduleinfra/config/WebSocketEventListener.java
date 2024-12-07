package com.wsws.moduleinfra.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.stereotype.Component;

@Component
public class WebSocketEventListener {

    // WebSocket 연결 성공 시 호출되는 메서드
    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String chatRoomId  = (String) headerAccessor.getSessionAttributes().get("chatRoomId");

        // 연결된 사용자 정보 출력
        System.out.println("STOMP 연결 성공! ChatRoom ID: " + chatRoomId);
    }

    // WebSocket 연결 끊어졌을 때 호출되는 메서드
    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        System.out.println("STOMP 연결 끊어짐: " + event.getSessionId());
    }
}
