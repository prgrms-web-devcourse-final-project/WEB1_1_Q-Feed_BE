package com.wsws.moduleapplication.follow.dto;

public record UserRecommendationDto(
        String userId,
        String nickname,
        String profileImage,
        int followerCount
) {
}
