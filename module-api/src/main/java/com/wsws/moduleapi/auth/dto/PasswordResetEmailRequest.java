package com.wsws.moduleapi.auth.dto;

import com.wsws.moduleapplication.authcontext.dto.PasswordResetRequestServiceDto;

public record PasswordResetEmailRequest(String email) {
    public PasswordResetRequestServiceDto toServiceDto() {
        return new PasswordResetRequestServiceDto(email);
    }
}
