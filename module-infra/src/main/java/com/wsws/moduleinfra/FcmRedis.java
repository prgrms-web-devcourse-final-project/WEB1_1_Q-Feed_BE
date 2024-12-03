package com.wsws.moduleinfra;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FcmRedis {

    @Qualifier("customRedisTemplateObject")
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String PREFIX = "FCM_TOKEN_";

    public void saveFcmToken(Long userId, String fcmToken) {
        String key = PREFIX + userId;
        redisTemplate.opsForValue().set(key, fcmToken);
    }

    public String getFcmToken(String redisKey) {
        return (String) redisTemplate.opsForValue().get(redisKey);
    }

    public void deleteFcmToken(Long userId) {
        String key = PREFIX + userId;
        redisTemplate.delete(key);
    }
}
