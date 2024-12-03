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

    public static ChatRoomEntity create(String userId, String userId2, LocalDateTime createdAt){
        ChatRoomEntity chatRoomEntity = new ChatRoomEntity();
        chatRoomEntity.userId = userId;
        chatRoomEntity.userId2 = userId2;
        chatRoomEntity.createdAt = createdAt;
        return chatRoomEntity;
    }

//    // 연관 관계 편의 메서드 (ChatMessage 추가 시 호출)
//    public void addMessage(ChatMessageEntity message) {
//        messages.add(message);
//        message.setChatRoom(this);  // 메시지가 추가될 때 ChatRoom도 설정
//    }

}
