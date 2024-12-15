package com.wsws.moduleinfra.authcontext.auth.repository;

import com.wsws.moduledomain.authcontext.auth.repo.VerificationCodeStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class RedisVerificationCodeStore implements VerificationCodeStore {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisVerificationCodeStore(@Qualifier("customRedisTemplateString") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Override
    public void saveCode(String key, String code, long ttl) {
        redisTemplate.opsForValue().set(key, code,ttl, TimeUnit.SECONDS);
    }

    @Override
    public Optional<String> getCode(String key) {
        String code = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(code);
    }

    @Override
    public void deleteCode(String key) {
        redisTemplate.delete(key);
    }
}
