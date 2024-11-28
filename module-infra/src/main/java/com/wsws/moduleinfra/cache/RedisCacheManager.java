package com.wsws.moduleinfra.cache;

import com.wsws.moduledomain.cache.CacheManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisCacheManager implements CacheManager {

    @Qualifier("redisTemplateInteger")
    private final RedisTemplate<String, Integer> redisTemplate;

    @Override
    public Integer get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void set(String key, Integer value, long ttlInMinutes) {
        redisTemplate.opsForValue().set(key, value, ttlInMinutes, TimeUnit.MINUTES);
    }
}
