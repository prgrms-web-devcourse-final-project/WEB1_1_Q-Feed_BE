package com.wsws.moduleinfra.entity.chat;

import com.wsws.moduledomain.chat.ChatMessage;
import com.wsws.moduledomain.chat.ChatRoom;
import com.wsws.moduledomain.chat.MessageType;
import com.wsws.moduledomain.chat.exception.UrlRequiredException;
import com.wsws.moduledomain.user.vo.UserId;
import com.wsws.moduledomain.chat.vo.Content;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", length = 100)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type;

    private String url;
    private Boolean isRead;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "user_id")
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatRoom_id", nullable = false)
    private ChatRoomEntity chatRoom;

    public static ChatMessageEntity create( String content, MessageType type, String url, Boolean isRead, LocalDateTime createdAt, String userId, ChatRoomEntity chatRoomEntity ){

        ChatMessageEntity chatMessageEntity = new ChatMessageEntity();
        chatMessageEntity.content = content;
        chatMessageEntity.type = type;
        chatMessageEntity.url = url;
        chatMessageEntity.isRead = isRead;
        chatMessageEntity.createdAt = createdAt;
        chatMessageEntity.userId = userId;
        chatMessageEntity.chatRoom = chatRoomEntity;
        return chatMessageEntity;
    }

    // 연관 관계 편의 메서드 (ChatRoom 설정 시 호출)
    public void setChatRoom(ChatRoomEntity chatRoom) {
        this.chatRoom = chatRoom;
    }

}

