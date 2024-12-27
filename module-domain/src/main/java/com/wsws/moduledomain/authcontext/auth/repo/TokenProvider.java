package com.wsws.moduledomain.authcontext.auth.repo;

import com.wsws.moduledomain.authcontext.auth.ParsedTokenInfo;

public interface TokenProvider {
    String createAccessToken(String userId, String role);
    String createRefreshToken(String userId, String role);
    boolean validateToken(String token);

    ParsedTokenInfo parseToken(String token);
}
