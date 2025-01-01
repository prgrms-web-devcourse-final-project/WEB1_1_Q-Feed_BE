package com.wsws.moduleinfra.cache;

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

}
