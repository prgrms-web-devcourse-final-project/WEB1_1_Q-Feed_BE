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
@NoArgsConstructor
public class ChatMessage {
    private Long id;
    private Content content;
    private MessageType type;
    private String url;
    private Boolean isRead;
    private LocalDateTime createdAt;
    private UserId userId;
    private Long chatRoomId;

//    // 생성자
//    public ChatMessage(Long id, Content content, MessageType type, String url, Boolean isRead, LocalDateTime createdAt, UserId userId, ChatRoom chatRoom) {
//        this.id = id;
//        this.content = content;
//        this.type = type;
//        this.url = url;
//        this.isRead = isRead;
//        this.createdAt = createdAt;
//        this.userId = userId;
//        this.chatRoom = chatRoom;
//    }
//
//    // 팩토리 메서드
//    public static ChatMessage create(String content, MessageType type, String url, UserId userId, ChatRoom chatRoom) {
//        if (type != MessageType.TEXT && (url == null || url.isEmpty())) {
//            throw new UrlRequiredException(); // 예외는 직접 정의해야 함
//        }
//        Content chatContent = Content.from(content);
//        return new ChatMessage(null,chatContent, type, url, false, LocalDateTime.now(), userId, chatRoom);
//    }

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