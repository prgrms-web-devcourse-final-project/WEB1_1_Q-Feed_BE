package com.wsws.moduleapplication.socialnetwork.recommendation.dto;

public record UserRecommendationResponse(
        String userId,
        String nickname,
        String profileImage,
        Long followerCount
) {
}
