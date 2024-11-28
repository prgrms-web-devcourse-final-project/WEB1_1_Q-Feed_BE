package com.wsws.moduleinfra.repo.auth;

import com.wsws.moduledomain.auth.RefreshToken;
import com.wsws.moduledomain.auth.repo.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisAuthRepository implements AuthRepository {

    @Qualifier("redisTemplateString")
    private final RedisTemplate<String, String> redisTemplate;


    @Override
    public Optional<RefreshToken> findByToken(String token) {
        String storedToken = redisTemplate.opsForValue().get(token);
        if (storedToken == null) {
            return Optional.empty();
        }
        return Optional.of(RefreshToken.create(storedToken, null)); // Expiry는 AuthService에서 관리
    }

    @Override
    public void save(RefreshToken refreshToken) {
        redisTemplate.opsForValue().set(refreshToken.getToken(), refreshToken.getToken(), 7, TimeUnit.DAYS);
    }

    @Override
    public void deleteByToken(String token) {
        redisTemplate.delete(token);
    }
}
