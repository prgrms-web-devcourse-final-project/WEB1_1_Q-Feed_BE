package com.wsws.moduleapi.dto.auth;

import com.wsws.moduleapplication.dto.auth.EmailVerificationServiceDto;

public record EmailVerificationRequest(String email) {
    public EmailVerificationServiceDto toServiceDto() {
        return new EmailVerificationServiceDto(email);
    }
}
