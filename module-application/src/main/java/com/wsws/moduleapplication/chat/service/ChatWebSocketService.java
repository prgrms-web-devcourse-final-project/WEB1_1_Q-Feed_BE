package com.wsws.moduleapplication.chat.service;

import com.wsws.modulecommon.service.RedisService;
import com.wsws.moduledomain.chat.ChatMessage;
import com.wsws.moduledomain.chat.ChatMessageDomainResponse;
import com.wsws.moduledomain.usercontext.user.aggregate.User;
import com.wsws.moduleinfra.redis.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketService {

    private final RedisTemplate<String, ChatMessage> redisTemplate;
    private final RedisSubscriber redisSubscriber;
    private final RedisService redisService;

    public void notifyWebSocketSubscribers(Long chatRoomId, ChatMessage chatMessage, User user) {
        try {
            if (!isRoomSubscribed(chatRoomId)) {
                redisSubscriber.subscribeToChatRoom(chatRoomId);
                markRoomAsSubscribed(chatRoomId);
            }

            ChatMessageDomainResponse response = ChatMessageDomainResponse.createFrom(chatMessage, user);
            log.info("ChatMessageDomainResponse{}", response);
            String channel = "/sub/chat/" + chatRoomId;
            redisTemplate.convertAndSend(channel, response);
        } catch (Exception e) {
            log.error("Failed to notify subscribers for chat room {}", chatRoomId, e);
        }
    }

    private boolean isRoomSubscribed(Long chatRoomId) {
        return redisService.isRoomSubscribed(chatRoomId);
    }

    private void markRoomAsSubscribed(Long chatRoomId) {
        redisService.markRoomAsSubscribed(chatRoomId);
    }
}
