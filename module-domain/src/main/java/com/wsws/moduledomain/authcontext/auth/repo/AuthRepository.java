package com.wsws.moduledomain.authcontext.auth.repo;

import com.wsws.moduledomain.authcontext.auth.RefreshToken;

import java.util.Optional;

public interface AuthRepository {
    Optional<RefreshToken> findByToken(String token);
    void save(RefreshToken refreshToken);
    void deleteByToken(String token);
}
