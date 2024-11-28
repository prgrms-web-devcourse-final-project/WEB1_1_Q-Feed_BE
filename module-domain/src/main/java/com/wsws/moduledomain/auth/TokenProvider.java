package com.wsws.moduledomain.auth;

public interface TokenProvider {
    String createAccessToken(String userId);
    String createRefreshToken(String userId);
    boolean validateToken(String token);
}
