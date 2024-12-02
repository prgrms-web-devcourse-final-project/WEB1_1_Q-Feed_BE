package com.wsws.moduleinfra.repo.auth;

import com.wsws.moduledomain.auth.RefreshToken;
import com.wsws.moduledomain.auth.repo.AuthRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository

public class RedisAuthRepository implements AuthRepository {


    private final RedisTemplate<String, String> redisTemplate;

    public RedisAuthRepository(@Qualifier("customRedisTemplateString") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String createRedisKey(String token) {
        return "auth:refreshToken:" + token;
    }


    @Override
    public Optional<RefreshToken> findByToken(String token) {
        String key = createRedisKey(token);
        String storedToken = redisTemplate.opsForValue().get(key);
        if (storedToken == null) {
            return Optional.empty();
        }
        return Optional.of(RefreshToken.create(storedToken, null)); // Expiry는 AuthService에서 관리
    }

    @Override
    public void save(RefreshToken refreshToken) {
        String key = createRedisKey(refreshToken.getToken());
        redisTemplate.opsForValue().set(key, refreshToken.getToken(), 7, TimeUnit.DAYS);
    }

    @Override
    public void deleteByToken(String token) {
        String key = createRedisKey(token);
        redisTemplate.delete(key);
    }
}
