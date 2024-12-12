package com.wsws.moduleapi.auth.dto;

import com.wsws.moduleapplication.authcontext.dto.EmailVerificationCheckServiceDto;

public record EmailVerificationCheckRequest(String email, String code) {
    public EmailVerificationCheckServiceDto toServiceDto() {
        return new EmailVerificationCheckServiceDto(email, code);
    }
}
