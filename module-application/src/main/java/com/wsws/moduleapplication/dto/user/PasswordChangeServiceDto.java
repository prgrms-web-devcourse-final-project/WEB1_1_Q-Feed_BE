package com.wsws.moduleapplication.dto.user;

import com.wsws.moduledomain.user.vo.UserId;
import lombok.Builder;

@Builder
public record PasswordChangeServiceDto(
        String currentPassword,
        String newPassword
) {
}
