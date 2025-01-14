package com.wsws.moduleinfra.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wsws.moduledomain.cache.CacheManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisKeyCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisCacheManager implements CacheManager {

    @Qualifier("customRedisTemplateObject")
    private final RedisTemplate<String, Object> redisTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T get(String key, Class<T> type) {
        Object cachedValue = redisTemplate.opsForValue().get(key);
        if (cachedValue != null) {
            return type.cast(cachedValue);
        }
        return null;
    }

    @Override
    public void set(String key, Object value, long ttlInMinutes) {
        redisTemplate.opsForValue().set(key, value, ttlInMinutes, TimeUnit.MINUTES);
    }

    @Override
    public void evict(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void evictAllByPrefix(String prefix) {
        Set<String> keysToDelete = new HashSet<>();
        ScanOptions scanOptions = ScanOptions.scanOptions()
                .match(prefix + "*")
                .count(10)
                .build();

        RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
        if (connectionFactory == null) {
            throw new IllegalStateException("RedisConnectionFactory is null");
        }

        try (var connection = connectionFactory.getConnection()) {
            RedisKeyCommands keyCommands = connection.keyCommands(); // Use keyCommands API
            try (var cursor = keyCommands.scan(scanOptions)) { // Iterate keys with Cursor
                while (cursor.hasNext()) {
                    keysToDelete.add(new String(cursor.next()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error during scanning keys by prefix", e);
        }

        if (!keysToDelete.isEmpty()) redisTemplate.delete(keysToDelete);
    }

    public void setJson(String key, Object value, long ttlInMinutes) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue, ttlInMinutes, TimeUnit.MINUTES);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Redis 직렬화 실패", e);
        }
    }

    public <T> T getJson(String key, TypeReference<T> typeRef) {
        Object cachedValue = redisTemplate.opsForValue().get(key);
        if (cachedValue == null) {
            return null;
        }
        String jsonValue = cachedValue.toString();
        try {
            return objectMapper.readValue(jsonValue, typeRef);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Redis 역직렬화 실패", e);
        }
    }

}
