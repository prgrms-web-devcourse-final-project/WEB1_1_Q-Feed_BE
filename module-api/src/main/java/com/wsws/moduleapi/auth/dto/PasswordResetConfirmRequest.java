package com.wsws.moduleapi.auth.dto;

import com.wsws.moduleapplication.auth.dto.PasswordResetConfirmServiceDto;

public record PasswordResetConfirmRequest(String email, String code, String newPassword) {
    public PasswordResetConfirmServiceDto toServiceDto() {
        return new PasswordResetConfirmServiceDto(email, code, newPassword);
    }
}
