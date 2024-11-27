package com.wsws.modulesecurity.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    public void storeRefreshToken(String userId, String refreshToken, long expiration) {
        redisTemplate.opsForValue().set(userId, refreshToken, expiration, TimeUnit.MILLISECONDS);
    }

    public boolean validateRefreshToken(String userId, String refreshToken) {
        String storedToken = redisTemplate.opsForValue().get(userId);
        return storedToken != null && storedToken.equals(refreshToken);
    }

    public void deleteRefreshToken(String userId) {
        redisTemplate.delete(userId);
    }
}
