package com.wsws.moduleinfra;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class FcmRedis {

    private final StringRedisTemplate stringRedisTemplate;

    private static final String PREFIX = "FCM_TOKEN_";

    public void saveFcmToken(String userId, String fcmToken, Duration time) {
        String key = PREFIX + userId;
        stringRedisTemplate.opsForValue().set(key, fcmToken, time);
    }

    public String getFcmToken(String redisKey) {
        return stringRedisTemplate.opsForValue().get(redisKey);
    }

    public void deleteFcmToken(String userId) {
        String key = PREFIX + userId;
        stringRedisTemplate.delete(key);
    }
}
