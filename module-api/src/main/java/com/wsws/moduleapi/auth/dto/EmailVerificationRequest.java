package com.wsws.moduleapi.auth.dto;

import com.wsws.moduleapplication.authcontext.dto.EmailVerificationServiceDto;

public record EmailVerificationRequest(String email) {
    public EmailVerificationServiceDto toServiceDto() {
        return new EmailVerificationServiceDto(email);
    }
}
