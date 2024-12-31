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
            log.info("채팅방 {}에 메시지를 Redis에 저장했습니다.", chatRoomId);
        } catch (Exception e) {
            log.error("채팅방 {}에 메시지를 Redis에 저장하는데 실패했습니다.", chatRoomId, e);
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
                    log.info("키 {}에 있는 {}개의 메시지를 DB에 저장했습니다.", key, messages.size());
                    redisTemplate.delete(key); // Redis에서 삭제
                    log.info("키 {}의 Redis 캐시 메시지를 삭제했습니다.", key);
                } catch (Exception e) {
                    log.error("키 {}의 메시지를 DB에 저장하는 중 오류가 발생했습니다. {}", key, e.getMessage());
                }
            }
        }
    }
}
