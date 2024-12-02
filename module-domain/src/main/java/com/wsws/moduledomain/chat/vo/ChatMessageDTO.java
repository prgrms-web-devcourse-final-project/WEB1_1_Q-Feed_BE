package com.wsws.moduledomain.chat.vo;

import com.wsws.moduledomain.chat.MessageType;
import com.wsws.moduledomain.user.vo.Nickname;
import com.wsws.moduledomain.user.vo.UserId;
import org.springframework.boot.autoconfigure.web.WebProperties;

import javax.swing.text.AbstractDocument;
import java.time.LocalDateTime;

public record ChatMessageDTO(
        Long messageId,
        String content,
        MessageType type,
        String url,
        Boolean isRead,
        LocalDateTime createdAt,
        String userId,
        String userNickName,
        String userProfileImage
) {
}