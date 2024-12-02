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
   // private List<ChatMessage> messages = new ArrayList<>();

    // 생성자 (userId, userId2를 받아 초기화)
//    public ChatRoom(Long id,String userId, String userId2, LocalDateTime createdAt) {
//        this.id = id;
//        this.userId = userId;
//        this.userId2 = userId2;
//        this.createdAt = createdAt;
//    }
//
//    // 팩토리 메서드 (userId < userId2 순서로 설정)
//    public static ChatRoom create(String userId, String userId2, LocalDateTime createdAt) {
//        if (userId.equals(userId2)) {
//            throw new SelfSelectionNotAllowedException(); // 예외는 직접 정의해야 함
//        }
//        if (userId.compareTo(userId2) > 0) {
//            return new ChatRoom(null, userId2, userId, createdAt);
//        } else {
//            return new ChatRoom(null, userId, userId2, createdAt);
//        }
//    }

    public static ChatRoom create(Long id, String userId, String userId2, LocalDateTime createdAt){
        // 사용자 ID가 같으면 예외를 발생시킴
        if (userId.equals(userId2)) {
            throw new SelfSelectionNotAllowedException();
        }
        if (userId.compareTo(userId2) > 0) {
            userId = userId2;
            userId2 = userId;
        }
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.id = id;
        chatRoom.userId = userId;
        chatRoom.userId2 = userId2;
        chatRoom.createdAt = createdAt;
        return chatRoom;
    }


}