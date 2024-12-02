package com.wsws.moduledomain.chat;

import com.wsws.moduledomain.chat.exception.UrlRequiredException;
import com.wsws.moduledomain.chat.vo.Content;
import com.wsws.moduledomain.user.vo.UserId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {
    private Long id;
    private Content content;
    private MessageType type;
    private String url;
    private Boolean isRead = false;
    private LocalDateTime createdAt;
    private UserId userId;
    private Long chatRoomId;

    // 생성자
    private ChatMessage(Content content, MessageType type, String url, Boolean isRead, LocalDateTime createdAt, UserId userId, Long chatRoomId) {
        this.content = content;
        this.type = type;
        this.url = url;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.userId = userId;
        this.chatRoomId = chatRoomId;
    }

    // 팩토리 메서드
    public static ChatMessage create(String content, MessageType type, String url, UserId userId, Long chatRoomId) {
        if (type != MessageType.TEXT && (url == null || url.isEmpty())) {
            throw new UrlRequiredException(); // 예외는 직접 정의해야 함
        }
        Content chatContent = Content.from(content);
        return new ChatMessage(chatContent, type, url, false, LocalDateTime.now(), userId, chatRoomId);
    }

    // 읽음 처리 메서드
    public void markAsRead() {
        if (!this.isRead) {
            this.isRead = true;
        }
    }
}