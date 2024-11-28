package com.wsws.moduleapplication.user.dto;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record RegisterUserRequest(
        String email,
        String password,
        String nickname,
        MultipartFile profileImageFile
) {
}
