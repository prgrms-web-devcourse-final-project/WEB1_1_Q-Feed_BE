package com.wsws.moduledomain.chat.vo;

import com.wsws.moduledomain.chat.MessageType;
import org.apache.logging.log4j.message.Message;

import java.time.LocalDateTime;

public record ChatMessageInfraDto(
        Long id,
        String content,
        MessageType type,
        String url,
        Boolean isRead,
        LocalDateTime createdAt,
        String userId,
        Long chatRoomId
) {
}
