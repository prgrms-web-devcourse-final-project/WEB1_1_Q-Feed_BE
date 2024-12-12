package com.wsws.moduleapplication.authcontext.dto;

public record LoginServiceResponse(
        String accessToken,
        String refreshToken,
        String userId
) {}
