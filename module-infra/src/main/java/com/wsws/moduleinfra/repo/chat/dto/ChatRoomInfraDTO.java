package com.wsws.moduleinfra.repo.chat.dto;

import com.wsws.moduledomain.user.vo.Nickname;

import java.time.LocalDateTime;

public record ChatRoomInfraDTO(
        Long chatRoomId,            // ChatRoom ID
        String otherUserNickname,   // 상대방의 닉네임
        String otherUserProfile,    // 상대방의 프로필 이미지
        String lastMessageContent,  // 마지막 메시지 내용
        LocalDateTime lastMessageCreatedAt  // 마지막 메시지 시간
) {}