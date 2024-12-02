package com.wsws.moduleapi.chat.dto;

import com.wsws.moduleapplication.chat.dto.ChatRoomServiceResponse;
import com.wsws.moduledomain.chat.vo.Content;
import com.wsws.moduledomain.user.vo.Nickname;

import java.time.LocalDateTime;

public record ChatRoomApiResponse(
        Long chatRoomId,
        Nickname otherUserNickname,
        String otherUserProfile,
        Content lastMessageContent,
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


