package com.wsws.moduleinfra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmRedis {

    private final StringRedisTemplate stringRedisTemplate;

    private static final String PREFIX = "FCM_TOKEN_";
    private static final  String NOTIFICATION_PREFIX = "FCM_NOTIFICATION_";

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

    public void pushNotification(String userId, String jsonMessage) {
        String key = NOTIFICATION_PREFIX + userId;
        stringRedisTemplate.opsForList().rightPush(key, jsonMessage);
        stringRedisTemplate.expire(key, Duration.ofMinutes(1));
    }

    public List<String> getNotifications(String userId) {
        String key = NOTIFICATION_PREFIX + userId;
        return stringRedisTemplate.opsForList().range(key, 0, -1);
    }

    public Set<String> getAllNotificationKeys() {
        return stringRedisTemplate.keys(NOTIFICATION_PREFIX + "*");
    }

    public void deleteNotifications(String userId) {
        String key = NOTIFICATION_PREFIX + userId;
        stringRedisTemplate.delete(key);
    }
}