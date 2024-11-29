package com.wsws.moduleapplication.dto.chat;

import com.wsws.moduledomain.chat.MessageType;
import com.wsws.moduledomain.user.vo.Nickname;
import com.wsws.moduledomain.user.vo.UserId;

import java.time.LocalDateTime;

public record ChatMessageServiceResponse(
        Long messageId,
        String content,
        MessageType type,
        String url,
        Boolean isRead,
        LocalDateTime createdAt,
        UserId userId,
        Nickname userNickName,
        String userProfileImage
) {
}
