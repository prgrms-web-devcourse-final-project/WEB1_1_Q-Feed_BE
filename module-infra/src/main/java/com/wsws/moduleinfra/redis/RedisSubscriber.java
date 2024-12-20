package com.wsws.moduleinfra.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wsws.moduledomain.chat.ChatMessageDomainResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;
    private final RedisMessageListenerContainer redisMessageListenerContainer;


    @Autowired
    public RedisSubscriber(@Qualifier("customRedisTemplateString") RedisTemplate<String, String> redisTemplate,
                           ObjectMapper objectMapper,
                           SimpMessageSendingOperations messagingTemplate, @Lazy RedisMessageListenerContainer redisMessageListenerContainer) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper.registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.messagingTemplate = messagingTemplate;
        this.redisMessageListenerContainer = redisMessageListenerContainer;

    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            System.out.println("발행된 메시지: " + publishMessage);  // 메시지 확인
            ChatMessageDomainResponse roomMessage = objectMapper.readValue(publishMessage, ChatMessageDomainResponse.class);
            System.out.println("시간확인: " + roomMessage);  // 메시지 확인
           messagingTemplate.convertAndSend("/sub/chat/" + roomMessage.chatRoomId(), roomMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 채팅방을 구독하는 메소드 (동적으로 구독 추가)
    public void subscribeToChatRoom(Long chatRoomId) {
        String channel = "/sub/chat/" + chatRoomId;
        redisMessageListenerContainer.addMessageListener(this, new ChannelTopic(channel));
    }

    // 구독을 해제하는 메소드 (선택 사항)
    public void unsubscribeFromChatRoom(Long chatRoomId) {
        String channel = "/sub/chat/" + chatRoomId;
        redisMessageListenerContainer.removeMessageListener(this, new ChannelTopic(channel));
    }
}