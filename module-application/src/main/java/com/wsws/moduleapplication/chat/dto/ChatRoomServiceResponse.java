package com.wsws.moduleapplication.chat.dto;

import com.wsws.moduleapplication.dto.user.UserProfileResponse;
import com.wsws.moduledomain.user.vo.Nickname;

import java.time.LocalDateTime;

public record ChatRoomServiceResponse(
        Long chatRoomId,
        Nickname otherUserNickname,
        String otherUserProfile,
        String lastMessageContent,
        LocalDateTime lastMessageCreatedAt
) {

}