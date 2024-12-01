package com.wsws.moduleapplication.chat.dto;

import com.wsws.moduledomain.chat.vo.Content;
import com.wsws.moduledomain.user.vo.Nickname;

import javax.swing.text.AbstractDocument;
import java.time.LocalDateTime;

public record ChatRoomServiceResponse(
        Long chatRoomId,
        Nickname otherUserNickname,
        String otherUserProfile,
        Content lastMessageContent,
        LocalDateTime lastMessageCreatedAt
) {

}