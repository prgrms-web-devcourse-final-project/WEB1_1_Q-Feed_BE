package com.wsws.moduleinfra.redis;

import com.wsws.modulecommon.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisServiceImpl implements RedisService {

    private static final Duration DEFAULT_TTL = Duration.ofHours(1);
    private final RedisTemplate<String, String> redisTemplate;

    public RedisServiceImpl(@Qualifier("customRedisTemplateString") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveUserRoomMapping(String userId, String roomId) {
        redisTemplate.opsForValue().set(getUserKey(userId), roomId, DEFAULT_TTL);
    }

    @Override
    public String getUserCurrentRoom(String userId) {
        return redisTemplate.opsForValue().get(getUserKey(userId));
    }

    @Override
    public void removeUserRoomMapping(String userId) {
        redisTemplate.delete(getUserKey(userId));
    }

    @Override
    public void saveSessionUserMapping(String sessionId, String userId) {
        redisTemplate.opsForValue().set(getSessionKey(sessionId), userId, DEFAULT_TTL);
    }

    @Override
    public String getUserBySessionId(String sessionId) {
        return redisTemplate.opsForValue().get(getSessionKey(sessionId));
    }

    @Override
    public void removeSessionUserMapping(String sessionId) {
        redisTemplate.delete(getSessionKey(sessionId));
    }

    private String getSessionKey(String sessionId) {
        return "session:" + sessionId;
    }

    private String getUserKey(String userId) {
        return "userId:" + userId;
    }

}
