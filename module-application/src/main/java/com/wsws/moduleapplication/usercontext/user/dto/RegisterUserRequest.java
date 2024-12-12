package com.wsws.moduleapplication.usercontext.user.dto;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
public record RegisterUserRequest(
        String email,
        String password,
        String nickname,
        MultipartFile profileImageFile,
        String description,
        List<String> interestCategoryNames
) {
}
