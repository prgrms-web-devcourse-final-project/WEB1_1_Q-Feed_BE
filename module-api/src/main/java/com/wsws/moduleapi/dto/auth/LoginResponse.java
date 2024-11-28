package com.wsws.moduleapi.dto.auth;

import com.wsws.moduleapplication.dto.auth.LoginServiceResponse;

public record LoginResponse(String accessToken, String refreshToken) {
    public LoginResponse(LoginServiceResponse response) {
        this(response.accessToken(), response.refreshToken());
    }
}
