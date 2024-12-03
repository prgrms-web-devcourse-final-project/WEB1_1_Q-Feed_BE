package com.wsws.moduledomain.chat;

import com.wsws.moduledomain.chat.exception.SelfSelectionNotAllowedException;
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
    private Boolean isRead;
    private LocalDateTime createdAt;
    private UserId userId;
    private Long chatRoomId;



    public static ChatMessage create(Long id, String content, MessageType type, String url, Boolean isRead, LocalDateTime createdAt, String userId, Long chatRoomId){
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.id = id;
        chatMessage.content = Content.from(content);
        chatMessage.type = type;
        chatMessage.url = url;
        chatMessage.isRead = isRead;
        chatMessage.createdAt = createdAt;
        chatMessage.userId = UserId.of(userId);
        chatMessage.chatRoomId = chatRoomId;
        return chatMessage;
    }


    // 읽음 처리 메서드
    public void markAsRead() {
        if (!this.isRead) {
            this.isRead = true;
        }
    }
    public boolean isRead() {
        return isRead;
    }
}