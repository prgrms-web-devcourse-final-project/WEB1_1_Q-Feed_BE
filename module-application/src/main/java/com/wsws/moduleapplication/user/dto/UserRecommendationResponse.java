package com.wsws.moduleapplication.user.dto;

public record UserRecommendationResponse(
        String userId,
        String nickname,
        String profileImage,
        Long followerCount
) {
}
