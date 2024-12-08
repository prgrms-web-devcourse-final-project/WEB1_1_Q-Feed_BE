package com.wsws.moduleinfra.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    private MessageListenerAdapter messageListenerAdapter;

    @Autowired
    public RedisSubscriber(@Qualifier("customRedisTemplateString") RedisTemplate<String, String> redisTemplate,
                           ObjectMapper objectMapper,
                           SimpMessageSendingOperations messagingTemplate, @Lazy RedisMessageListenerContainer redisMessageListenerContainer) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.messagingTemplate = messagingTemplate;
        this.redisMessageListenerContainer = redisMessageListenerContainer;

    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            System.out.println("발행된 메시지: " + publishMessage);  // 메시지 확인
            ChatMessageDomainResponse roomMessage = objectMapper.readValue(publishMessage, ChatMessageDomainResponse.class);
            System.out.println("!@@@@@@!!!!!!!!!!발행된 메시지!!!!!!!: " + roomMessage);
           messagingTemplate.convertAndSend("/sub/chat/" + roomMessage.chatRoomId(), roomMessage);
            System.out.println("@@@@@@@@@@@@REDIS로 보냄!!!!!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 채팅방을 구독하는 메소드 (동적으로 구독 추가)
    public void subscribeToChatRoom(Long chatRoomId) {
        System.out.println("@@@@@@@@@@@@채팅방 구독!!!!!");
        String channel = "/sub/chat/" + chatRoomId;
        redisMessageListenerContainer.addMessageListener(messageListenerAdapter, new ChannelTopic(channel));
        System.out.println("~~~~~~~~~~~~~~~채널 구독 완료!!");
    }

    // 구독을 해제하는 메소드 (선택 사항)
    public void unsubscribeFromChatRoom(Long chatRoomId) {
        String channel = "/sub/chat/" + chatRoomId;
        redisMessageListenerContainer.removeMessageListener(this, new ChannelTopic(channel));
    }
}