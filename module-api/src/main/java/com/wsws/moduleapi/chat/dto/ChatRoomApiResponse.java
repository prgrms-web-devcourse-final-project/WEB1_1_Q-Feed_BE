package com.wsws.moduleapi.chat.dto;

import com.wsws.moduleapplication.chat.dto.ChatRoomServiceResponse;

import java.time.LocalDateTime;

public record ChatRoomApiResponse(
        Long chatRoomId,
        String otherUserNickname,
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


