package com.wsws.moduleapi.follow.dto;

import com.wsws.moduleapplication.follow.dto.FollowServiceResponseDto;

import java.time.LocalDateTime;


public record FollowResponseDto(
        String userId,
        String nickname,
        String profileImage,
        LocalDateTime createdAt
) {
    // Application DTO로 변환
    public static FollowResponseDto fromServiceDto(FollowServiceResponseDto serviceDto) {
        return new FollowResponseDto(
                serviceDto.userId(),
                serviceDto.nickname(),
                serviceDto.profileImage(),
                serviceDto.createAt()
        );
    }
}