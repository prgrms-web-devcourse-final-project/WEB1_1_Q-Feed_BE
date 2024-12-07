package com.wsws.moduleapplication.chat.dto;

import com.wsws.moduledomain.chat.MessageType;
import com.wsws.moduledomain.chat.vo.Content;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public record WebSocketChatRequest(
        Long roomId,
        String senderId,
        String message
//        MessageType type,
//        MultipartFile url,
//        LocalDateTime createdAt
        ) {
    public ChatMessageRequest toChatMessageRequest() {
        return new ChatMessageRequest(this.message(),MessageType.TEXT,null);
    }

}
