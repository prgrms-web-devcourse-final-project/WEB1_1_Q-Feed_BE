package com.wsws.moduleapplication.socialnetwork.recommendation.mapper;


import com.wsws.moduleapplication.socialnetwork.recommendation.dto.UserRecommendationResponse;
import com.wsws.moduledomain.follow.vo.UserRecommendation;

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