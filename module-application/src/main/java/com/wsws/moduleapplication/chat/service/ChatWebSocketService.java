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
                log.info("채팅방 {}에 구독을 추가했습니다.", chatRoomId);
            }

            ChatMessageDomainResponse response = ChatMessageDomainResponse.createFrom(chatMessage, user);
            log.info("채팅 메시지 응답 생성: {}", response);
            String channel = "/sub/chat/" + chatRoomId;
            redisTemplate.convertAndSend(channel, response);
            log.info("채팅 메시지가 채널 {}에 전송되었습니다: {}", channel, response);
        } catch (Exception e) {
            log.error("채팅방 {}의 구독자에게 메세지를 보내는 데 실패했습니다.", chatRoomId, e);
        }
    }

    private boolean isRoomSubscribed(Long chatRoomId) {
        return redisService.isRoomSubscribed(chatRoomId);
    }

    private void markRoomAsSubscribed(Long chatRoomId) {
        redisService.markRoomAsSubscribed(chatRoomId);
    }
}
