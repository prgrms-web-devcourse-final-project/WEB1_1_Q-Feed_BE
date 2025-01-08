package com.wsws.moduleapi.auth.dto;

public record LogoutRequest(
        String userId,
        String refreshToken
) {
}
