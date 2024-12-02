package com.wsws.moduleinfra.repo.chat;


import com.wsws.moduledomain.chat.ChatRoom;
import com.wsws.moduledomain.chat.repo.ChatRoomRepository;
import com.wsws.moduleinfra.entity.chat.ChatRoomEntity;
import com.wsws.moduleinfra.repo.chat.mapper.ChatRoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepository {

    private final JpaChatRoomRepository jpaChatRoomRepository;

    @Override
    public void save(ChatRoom chatRoom) {
        ChatRoomEntity entity = ChatRoomMapper.toEntity(chatRoom);
        jpaChatRoomRepository.save(entity);
    }

    @Override
    public void delete(ChatRoom chatRoom) {
        ChatRoomEntity entity = ChatRoomMapper.toEntity(chatRoom);
        jpaChatRoomRepository.delete(entity);
    }

    @Override
    public Optional<ChatRoom> findChatRoomBetweenUsers(String userId, String userId2) {
        return jpaChatRoomRepository.findChatRoomBetweenUsers(userId, userId2)
                .map(ChatRoomMapper::toDomain);
    }

    @Override
    public Optional<ChatRoom> findChatRoomById(Long chatRoomId) {
        return jpaChatRoomRepository.findById(chatRoomId)
                .map(ChatRoomMapper::toDomain);
    }

    @Override
    public List<ChatRoom> findChatRooms(String userId) {
        return jpaChatRoomRepository.findChatRooms(userId).stream()
                .map(ChatRoomMapper::toDomain)
                .toList();
    }
}
