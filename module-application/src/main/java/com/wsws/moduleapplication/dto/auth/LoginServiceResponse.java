package com.wsws.moduleapplication.dto.auth;

public record LoginServiceResponse(
        String accessToken,
        String refreshToken
) {}
