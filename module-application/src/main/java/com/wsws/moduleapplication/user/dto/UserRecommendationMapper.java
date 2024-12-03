package com.wsws.moduleapplication.user.dto;


import com.wsws.moduledomain.recommendation.UserRecommendation;

import java.util.List;

public class UserRecommendationMapper {

    public static UserRecommendationResponse toDto(UserRecommendation recommendation) {
        return new UserRecommendationResponse(
                recommendation.getUserId(),
                recommendation.getNickname(),
                recommendation.getProfileimage(),
                recommendation.getFollowerCount()
        );
    }

    public static List<UserRecommendationResponse> toDtoList(List<UserRecommendation> recommendations) {
        return recommendations.stream()
                .map(UserRecommendationMapper::toDto)
                .toList();
    }
}