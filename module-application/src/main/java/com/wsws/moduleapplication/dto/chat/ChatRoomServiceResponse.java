package com.wsws.moduleapplication.dto.chat;

import com.wsws.moduleapplication.dto.user.UserProfileResponse;
import com.wsws.moduledomain.chat.ChatMessage;
import com.wsws.moduledomain.chat.ChatRoom;
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