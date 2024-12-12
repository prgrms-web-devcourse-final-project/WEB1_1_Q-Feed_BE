package com.wsws.moduleapplication.usercontext.user.dto;

import lombok.Builder;

@Builder
public record PasswordChangeServiceDto(
        String currentPassword,
        String newPassword
) {
}
