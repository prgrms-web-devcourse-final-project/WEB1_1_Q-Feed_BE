package com.wsws.moduleapi.auth.dto;

import com.wsws.moduleapplication.auth.dto.EmailVerificationServiceDto;

public record EmailVerificationRequest(String email) {
    public EmailVerificationServiceDto toServiceDto() {
        return new EmailVerificationServiceDto(email);
    }
}
