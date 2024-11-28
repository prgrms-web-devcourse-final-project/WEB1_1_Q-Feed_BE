package com.wsws.moduleapi.dto.auth;

import com.wsws.moduleapplication.dto.auth.EmailVerificationCheckServiceDto;

public record EmailVerificationCheckRequest(String email, String code) {
    public EmailVerificationCheckServiceDto toServiceDto() {
        return new EmailVerificationCheckServiceDto(email, code);
    }
}
