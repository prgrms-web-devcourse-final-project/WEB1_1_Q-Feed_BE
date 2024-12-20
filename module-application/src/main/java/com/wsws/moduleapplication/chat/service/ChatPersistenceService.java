package com.wsws.moduleapplication.chat.service;

import com.wsws.moduledomain.chat.ChatMessage;
import com.wsws.moduledomain.chat.repo.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatPersistenceService {

    private final RedisTemplate<String, ChatMessage> redisTemplate;
    private final ChatMessageRepository chatMessageRepository;

    //레디스에 채팅메세지 저장
    @Async
    public void saveMessageInRedisAsync(Long chatRoomId, ChatMessage chatMessage) {
        try {
            String key = "chatRoomId:messages:" + chatRoomId;
            redisTemplate.opsForList().rightPush(key, chatMessage);
            log.info("Cached message in Redis for room {}", chatRoomId);
        } catch (Exception e) {
            log.error("Failed to cache message in Redis for room {}", chatRoomId, e);
        }
    }

    @Scheduled(fixedRate = 5000) // 5초마다 실행
    public void persistCachedMessages() {
        Set<String> keys = redisTemplate.keys("chatRoomId:messages:*");
        if (keys == null || keys.isEmpty()) return;

        for (String key : keys) {
            List<ChatMessage> messages = redisTemplate.opsForList().range(key, 0, -1);
            if (messages != null && !messages.isEmpty()) {
                try {
                    chatMessageRepository.saveAll(messages); // 배치 저장
                    log.info("Persisted {} messages to DB for key {}", messages.size(), key);
                    redisTemplate.delete(key); // Redis에서 삭제
                    log.info("Deleted cached messages from Redis for key {}", key);
                } catch (Exception e) {
                    log.error("Failed to persist messages to database for key {}", key, e);
                }
            }
        }
    }
}
