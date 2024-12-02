package com.wsws.moduleinfra.repo.chat.mapper;

import com.wsws.moduledomain.chat.ChatRoom;
import com.wsws.moduleinfra.entity.chat.ChatRoomEntity;

public class ChatRoomMapper {

    // Entity -> Domain 변환
    public static ChatRoom toDomain(ChatRoomEntity entity) {
        return ChatRoom.create(
                entity.getId(),
                entity.getUserId(),
                entity.getUserId2(),
                entity.getCreatedAt()
        );
    }
    // Domain -> Entity 변환
    public static ChatRoomEntity toEntity(ChatRoom chatRoom) {
        return ChatRoomEntity.create(
                chatRoom.getUserId(),
                chatRoom.getUserId2(),
                chatRoom.getCreatedAt()
        );
    }
}
