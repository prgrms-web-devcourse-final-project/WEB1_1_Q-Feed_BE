package com.wsws.moduleapi.dto.auth;

import com.wsws.moduleapplication.dto.auth.PasswordResetConfirmServiceDto;

public record PasswordResetConfirmRequest(String email, String code, String newPassword) {
    public PasswordResetConfirmServiceDto toServiceDto() {
        return new PasswordResetConfirmServiceDto(email, code, newPassword);
    }
}
