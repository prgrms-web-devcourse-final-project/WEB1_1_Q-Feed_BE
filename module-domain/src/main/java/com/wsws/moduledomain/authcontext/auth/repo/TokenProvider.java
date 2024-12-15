package com.wsws.moduledomain.authcontext.auth.repo;

public interface TokenProvider {
    String createAccessToken(String userId);
    String createRefreshToken(String userId);
    boolean validateToken(String token);
}
