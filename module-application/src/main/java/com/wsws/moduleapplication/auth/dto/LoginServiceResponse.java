package com.wsws.moduleapplication.auth.dto;

public record LoginServiceResponse(
        String accessToken,
        String refreshToken
) {}
