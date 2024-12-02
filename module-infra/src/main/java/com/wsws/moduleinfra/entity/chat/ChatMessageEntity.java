package com.wsws.moduleinfra.entity.chat;

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

    @Column(name = "content", nullable = false, unique = true, length = 100)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type;

    private String url;

    private Boolean isRead = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "user_id")
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatRoom_id", nullable = false)
    private ChatRoomEntity chatRoom;

    private ChatMessageEntity(String content, MessageType type, String url, Boolean isRead, LocalDateTime createdAt, String userId, ChatRoomEntity chatRoom) {
        this.content = content;
        this.type = type;
        this.url = url;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.userId = userId;
        this.chatRoom = chatRoom;
    }

    public static ChatMessageEntity createChatMessage(String content, MessageType type, String url, String userId, ChatRoomEntity chatRoom) {
        if (type != MessageType.TEXT && (url == null || url.isEmpty())) {
            throw UrlRequiredException.EXCEPTION;
        }
       // Content chatContent = Content.from(content);

        return new ChatMessageEntity(content, type, url, false, LocalDateTime.now(), userId, chatRoom);
    }

    public void markMessageAsRead() {
        if (!this.isRead) {
            this.isRead = true;
        }
    }
}

