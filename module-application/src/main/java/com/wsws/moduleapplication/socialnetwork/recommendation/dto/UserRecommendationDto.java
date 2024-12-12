package com.wsws.moduleapplication.socialnetwork.recommendation.dto;

public record UserRecommendationDto(
        String userId,
        String nickname,
        String profileImage,
        int followerCount
) {
}
