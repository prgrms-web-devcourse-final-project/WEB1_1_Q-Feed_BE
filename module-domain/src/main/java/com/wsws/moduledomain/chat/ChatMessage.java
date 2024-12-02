package com.wsws.moduledomain.chat;

import com.wsws.moduledomain.chat.exception.UrlRequiredException;
import com.wsws.moduledomain.chat.vo.Content;
import com.wsws.moduledomain.user.vo.UserId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Content content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type;

    private String url;

    private Boolean isRead = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "sender_id", nullable = false))
    private UserId userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="chatRoom_id", nullable=false)
    private ChatRoom chatRoom;

    private ChatMessage(Content content, MessageType type, String url, Boolean isRead, LocalDateTime createdAt, UserId userId, ChatRoom chatRoom) {
        this.content = content;
        this.type = type;
        this.url = url;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.userId = userId;
        this.chatRoom = chatRoom;
    }

    public static ChatMessage create(String content, MessageType type, String url, UserId userId, ChatRoom chatRoom) {
        if (type != MessageType.TEXT && (url == null || url.isEmpty())) {
            throw UrlRequiredException.EXCEPTION;
        }
        Content chatContent = Content.from(content);

        return new ChatMessage(chatContent, type, url, false, LocalDateTime.now(), userId, chatRoom);
    }

    public void markAsRead(){
        if (!this.isRead) {
            this.isRead = true;
        }
    }
}