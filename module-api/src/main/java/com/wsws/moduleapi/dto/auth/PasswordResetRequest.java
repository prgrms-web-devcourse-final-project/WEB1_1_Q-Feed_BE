package com.wsws.moduleapi.dto.auth;

import com.wsws.moduleapplication.dto.auth.PasswordResetRequestServiceDto;

public record PasswordResetRequest(String email) {
    public PasswordResetRequestServiceDto toServiceDto() {
        return new PasswordResetRequestServiceDto(email);
    }
}
