package com.wsws.moduleapplication.usercontext.user.dto;


import org.springframework.web.multipart.MultipartFile;

public record UpdateProfileServiceDto(
        String nickname,
        String description,
        MultipartFile profileImageFile // MultipartFile로 이미지 파일 전달
) {}
