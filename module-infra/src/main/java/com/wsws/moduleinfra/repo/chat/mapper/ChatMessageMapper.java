package com.wsws.moduleinfra.repo.chat.mapper;

import com.wsws.moduledomain.chat.ChatMessage;
import com.wsws.moduleinfra.entity.chat.ChatMessageEntity;
import com.wsws.moduleinfra.entity.chat.ChatRoomEntity;

public class ChatMessageMapper {
    // Entity -> Domain 변환
    public static ChatMessage toDomain(ChatMessageEntity entity) {
        return ChatMessage.create(
                entity.getId(),
                entity.getContent(),
                entity.getType(),
                entity.getUrl(),
                entity.getIsRead(),
                entity.getCreatedAt(),
                entity.getUserId(),
                entity.getChatRoom().getId()
        );
    }

    // Domain -> Entity 변환
    public static ChatMessageEntity toEntity(ChatMessage chatMessage,ChatRoomEntity chatRoomEntity) {

        return ChatMessageEntity.create(
                chatMessage.getContent(),
                chatMessage.getType(),
                chatMessage.getUrl(),
                chatMessage.isRead(),
                chatMessage.getCreatedAt(),
                chatMessage.getUserId(),
                chatRoomEntity
        );
    }
}
