package com.wsws.moduleapi.dto.chat;

import com.wsws.moduleapplication.dto.chat.ChatRoomServiceResponse;
import com.wsws.moduleapplication.dto.user.UserProfileResponse;
import com.wsws.moduledomain.user.vo.Nickname;

import java.time.LocalDateTime;

public record ChatRoomApiResponse(
        Long chatRoomId,
        Nickname otherUserNickname,
        String otherUserProfile,
        String lastMessageContent,
        LocalDateTime lastMessageCreatedAt
) {
    //serviceresponse->apiresponseResponse
    public ChatRoomApiResponse(ChatRoomServiceResponse serviceResponse) {
        this(
                serviceResponse.chatRoomId(),
                serviceResponse.otherUserNickname(),
                serviceResponse.otherUserProfile(),
                serviceResponse.lastMessageContent(),
                serviceResponse.lastMessageCreatedAt()
        );
    }
}


