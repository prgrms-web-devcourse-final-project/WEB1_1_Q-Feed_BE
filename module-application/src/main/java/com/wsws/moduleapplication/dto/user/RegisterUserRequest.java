package com.wsws.moduleapplication.dto.user;

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
