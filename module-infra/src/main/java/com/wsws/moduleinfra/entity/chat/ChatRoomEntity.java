package com.wsws.moduleinfra.entity.chat;

import com.wsws.moduledomain.chat.ChatRoom;
import com.wsws.moduledomain.chat.exception.SelfSelectionNotAllowedException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String userId2;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessageEntity> messages = new ArrayList<>();

//    public ChatRoomEntity(String userId, String userId2, LocalDateTime createdAt) {
//        this.userId = userId;
//        this.userId2 = userId2;
//        this.createdAt = createdAt;
//    }
//
//    public static ChatRoomEntity createChatRoom(String userId, String userId2, LocalDateTime createdAt) {
//        if (userId.equals(userId2)) {
//            throw SelfSelectionNotAllowedException.EXCEPTION;
//        }
//
//        if (userId.compareTo(userId2) > 0) {
//            return new ChatRoomEntity(userId2, userId, createdAt);
//        } else {
//            return new ChatRoomEntity(userId, userId2, createdAt);
//        }
//    }

    public static ChatRoomEntity create(String userId, String userId2, LocalDateTime createdAt){
        ChatRoomEntity chatRoomEntity = new ChatRoomEntity();
        chatRoomEntity.userId = userId;
        chatRoomEntity.userId2 = userId2;
        chatRoomEntity.createdAt = createdAt;
        return chatRoomEntity;
    }
}
