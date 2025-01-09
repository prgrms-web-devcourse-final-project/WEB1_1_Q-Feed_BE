package com.wsws.moduleapplication.socialnetwork.recommendation.mapper;


import com.wsws.moduleapplication.socialnetwork.recommendation.dto.UserRecommendationResponse;
import com.wsws.moduledomain.socialnetwork.recommendation.Recommendation;

import java.util.List;

public class UserRecommendationMapper {

    public static UserRecommendationResponse toDto(Recommendation recommendation) {
        return new UserRecommendationResponse(
                recommendation.getUserId(),
                recommendation.getNickname(),
                recommendation.getProfileimage(),
                recommendation.getFollowerCount()
        );
    }

    public static List<UserRecommendationResponse> toDtoList(List<Recommendation> recommendations) {
        return recommendations.stream()
                .map(UserRecommendationMapper::toDto)
                .toList();
    }
}