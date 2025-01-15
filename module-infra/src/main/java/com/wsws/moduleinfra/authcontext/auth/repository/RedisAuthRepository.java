package com.wsws.moduleinfra.authcontext.auth.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wsws.moduledomain.authcontext.auth.RefreshToken;
import com.wsws.moduledomain.authcontext.auth.repo.AuthRepository;
import com.wsws.moduleinfra.authcontext.auth.dto.RefreshTokenData;
import com.wsws.moduleinfra.authcontext.auth.exception.InvalidRefreshTokenDataException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;


import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository

public class RedisAuthRepository implements AuthRepository {


    private final RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper;


    public RedisAuthRepository(@Qualifier("customRedisTemplateString") RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    private String createRedisKey(String token) {
        return "auth:refreshToken:" + token;
    }


    @Override
    public Optional<RefreshToken> findByToken(String token) {
        String key = createRedisKey(token);
        String storedValue = redisTemplate.opsForValue().get(key);
        if (storedValue == null) {
            return Optional.empty();
        }


        try {
            RefreshTokenData data = objectMapper.readValue(storedValue, RefreshTokenData.class);
            RefreshToken refreshToken = RefreshToken.create(data.getToken(), data.getExpiryDate());
            return Optional.of(refreshToken);
        } catch (IOException e) {
            throw InvalidRefreshTokenDataException.EXCEPTION;
        }
    }

    @Override
    public void save(RefreshToken refreshToken) {
        String key = createRedisKey(refreshToken.getToken());

        RefreshTokenData data = new RefreshTokenData(
                refreshToken.getToken(),
                refreshToken.getExpiryDate()
        );

        try {
            String json = objectMapper.writeValueAsString(data);
            redisTemplate.opsForValue().set(key, json, 7, TimeUnit.DAYS);
        } catch (IOException e) {
            throw InvalidRefreshTokenDataException.EXCEPTION;
        }
    }

    @Override
    public void deleteByToken(String token) {
        String key = createRedisKey(token);
        redisTemplate.delete(key);
    }
}
