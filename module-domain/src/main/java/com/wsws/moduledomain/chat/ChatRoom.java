package com.wsws.moduledomain.chat;

import com.wsws.moduledomain.chat.exception.SelfSelectionNotAllowedException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {
    private Long id;
    private String userId;
    private String userId2;
    private LocalDateTime createdAt;

    // 생성자 (userId, userId2를 받아 초기화)
    public ChatRoom(String userId, String userId2) {
        this.userId = userId;
        this.userId2 = userId2;
        this.createdAt = LocalDateTime.now();
    }

    // 팩토리 메서드 (userId < userId2 순서로 설정)
    public static ChatRoom create(String userId, String userId2) {
        if (userId.equals(userId2)) {
            throw new SelfSelectionNotAllowedException(); // 예외는 직접 정의해야 함
        }

        // 항상 userId < userId2 순서로 설정
        if (userId.compareTo(userId2) > 0) {
            // userId가 userId2보다 클 경우 순서를 바꿈
            return new ChatRoom(userId2, userId);
        } else {
            // userId가 userId2보다 작거나 같을 경우 그대로 사용
            return new ChatRoom(userId, userId2);
        }
    }
}