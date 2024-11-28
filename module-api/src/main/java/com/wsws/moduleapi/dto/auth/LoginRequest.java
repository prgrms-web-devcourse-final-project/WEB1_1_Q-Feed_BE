package com.wsws.moduleapi.dto.auth;

import com.wsws.moduleapplication.dto.auth.LoginServiceRequest;

public record LoginRequest(String email, String password) {
    public LoginServiceRequest toServiceDto() {
        return new LoginServiceRequest(email, password);
    }
}
