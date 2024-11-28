package com.wsws.moduleapplication.user.dto;

import lombok.Builder;

@Builder
public record PasswordChangeServiceDto(
        String currentPassword,
        String newPassword
) {
}
