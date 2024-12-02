package com.wsws.moduleinfra.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wsws.moduledomain.chat.ChatMessageDomainResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());

            ChatMessageDomainResponse roomMessage = objectMapper.readValue(publishMessage, ChatMessageDomainResponse.class);

           messagingTemplate.convertAndSend("/sub/chat/room/" + roomMessage.chatRoomId(), roomMessage);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}