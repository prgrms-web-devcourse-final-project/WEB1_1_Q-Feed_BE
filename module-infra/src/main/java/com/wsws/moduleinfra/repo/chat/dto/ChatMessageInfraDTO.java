package com.wsws.moduleinfra.repo.chat.dto;

import com.wsws.moduledomain.chat.MessageType;
import com.wsws.moduledomain.chat.vo.Content;
import com.wsws.moduledomain.user.vo.Nickname;
import com.wsws.moduledomain.user.vo.UserId;

import java.time.LocalDateTime;

public record ChatMessageInfraDTO(
        Long messageId,
        Content content,
        MessageType type,
        String url,
        Boolean isRead,
        LocalDateTime createdAt,
        UserId userId,
        Nickname userNickName,
        String userProfileImage
) {
}