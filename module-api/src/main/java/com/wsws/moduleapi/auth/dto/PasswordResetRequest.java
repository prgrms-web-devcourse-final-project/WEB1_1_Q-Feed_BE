package com.wsws.moduleapi.auth.dto;

import com.wsws.moduleapplication.authcontext.dto.PasswordResetConfirmServiceDto;

public record PasswordResetRequest(String email, String password) {

    public PasswordResetConfirmServiceDto toServiceDto() {
        return new PasswordResetConfirmServiceDto(email, password);

    }

}
