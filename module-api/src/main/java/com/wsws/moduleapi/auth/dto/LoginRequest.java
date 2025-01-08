package com.wsws.moduleapi.auth.dto;

import com.wsws.moduleapplication.authcontext.dto.LoginServiceRequest;

public record LoginRequest(String email, String password, String fcmToken) {
    public LoginServiceRequest toServiceDto() {
        return new LoginServiceRequest(email, password, fcmToken);
    }
}
