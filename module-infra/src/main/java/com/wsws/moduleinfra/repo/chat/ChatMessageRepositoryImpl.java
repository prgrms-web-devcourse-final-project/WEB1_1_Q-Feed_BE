package com.wsws.moduleinfra.repo.chat;

import com.wsws.moduledomain.chat.ChatMessage;
import com.wsws.moduledomain.chat.ChatRoom;
import com.wsws.moduledomain.chat.dto.ChatMessageDTO;
import com.wsws.moduledomain.chat.repo.ChatMessageRepository;
import com.wsws.moduledomain.chat.repo.ChatRoomRepository;
import com.wsws.moduleinfra.entity.chat.ChatMessageEntity;
import com.wsws.moduleinfra.entity.chat.ChatRoomEntity;
import com.wsws.moduleinfra.repo.chat.mapper.ChatMessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepository {

    private final JpaChatMessageRepository jpaChatMessageRepository;
    private final JpaChatRoomRepository jpaChatRoomRepository;

    @Override
    public void save(ChatMessage chatMessage) {
        // ChatMessage -> ChatMessageEntity 변환 후 저장
        ChatRoomEntity chatRoomEntity = jpaChatRoomRepository.findById(chatMessage.getChatRoomId())
                .orElseThrow();

        ChatMessageEntity entity = ChatMessageMapper.toEntity(chatMessage);
        entity.setChatRoom(chatRoomEntity); // 연관관계 설정
        jpaChatMessageRepository.save(entity);
    }

    @Override
    public void markAllMessagesAsRead(Long chatRoomId) {
        // 특정 채팅방에 대해 모든 메시지를 읽음 처리
        jpaChatMessageRepository.markAllMessagesAsRead(chatRoomId);
    }

    @Override
    public List<ChatMessageDTO> findMessagesWithUserDetails(Long chatRoomId, LocalDateTime cursor, int size) {
        Pageable pageable = PageRequest.of(0, size);
        return jpaChatMessageRepository.findMessagesWithUserDetails(chatRoomId, cursor,  pageable);
    }

    @Override
    public Optional<ChatMessage> findTopByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId) {
        // 최신 메시지 하나를 찾아서 반환
        return jpaChatMessageRepository.findTopByChatRoomIdOrderByCreatedAtDesc(chatRoomId).map(ChatMessageMapper::toDomain);
    }

    @Override
    public long countUnreadMessages(Long chatRoomId, String userId) {
        // 읽지 않은 메시지 수 카운트
        return jpaChatMessageRepository.countUnreadMessages(chatRoomId, userId);
    }

    @Override
    public void saveAll(List<ChatMessage> messages) {
        //모든 메시지가 동일한 chatRoomId
        Long chatRoomId = messages.get(0).getChatRoomId();

        ChatRoomEntity chatRoomEntity = jpaChatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("ChatRoom not found for ID " + chatRoomId));

        //ChatMessage -> ChatMessageEntity 변환
        List<ChatMessageEntity> entities = messages.stream()
                .map(chatMessage -> {
                    ChatMessageEntity entity = ChatMessageMapper.toEntity(chatMessage);
                    entity.setChatRoom(chatRoomEntity); // 연관관계 설정
                    return entity;
                }).toList();

        jpaChatMessageRepository.saveAll(entities);

    }

}
