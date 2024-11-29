package com.wsws.moduleapi.auth.dto;

import com.wsws.moduleapplication.auth.dto.PasswordResetRequestServiceDto;

public record PasswordResetRequest(String email) {
    public PasswordResetRequestServiceDto toServiceDto() {
        return new PasswordResetRequestServiceDto(email);
    }
}
