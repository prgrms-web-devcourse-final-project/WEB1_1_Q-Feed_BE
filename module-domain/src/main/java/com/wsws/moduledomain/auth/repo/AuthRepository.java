package com.wsws.moduledomain.auth.repo;

import com.wsws.moduledomain.auth.RefreshToken;

import java.util.Optional;

public interface AuthRepository {
    Optional<RefreshToken> findByToken(String token);
    void save(RefreshToken refreshToken);
    void deleteByToken(String token);
}
