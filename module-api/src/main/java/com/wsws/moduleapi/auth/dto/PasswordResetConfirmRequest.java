package com.wsws.moduleapi.auth.dto;

import com.wsws.moduleapplication.authcontext.dto.PasswordResetConfirmServiceDto;

public record PasswordResetConfirmRequest(String email, String code) {
    public PasswordResetConfirmServiceDto toServiceDto() {
        return new PasswordResetConfirmServiceDto(email, code);
    }
}
