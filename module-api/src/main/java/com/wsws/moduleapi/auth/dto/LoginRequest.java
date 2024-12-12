package com.wsws.moduleapi.auth.dto;

import com.wsws.moduleapplication.authcontext.dto.LoginServiceRequest;

public record LoginRequest(String email, String password) {
    public LoginServiceRequest toServiceDto() {
        return new LoginServiceRequest(email, password);
    }
}
