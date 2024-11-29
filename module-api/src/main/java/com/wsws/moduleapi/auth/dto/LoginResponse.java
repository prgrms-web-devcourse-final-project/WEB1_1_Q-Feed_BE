package com.wsws.moduleapi.auth.dto;

import com.wsws.moduleapplication.auth.dto.LoginServiceResponse;

public record LoginResponse(String accessToken, String refreshToken) {
    public LoginResponse(LoginServiceResponse response) {
        this(response.accessToken(), response.refreshToken());
    }
}