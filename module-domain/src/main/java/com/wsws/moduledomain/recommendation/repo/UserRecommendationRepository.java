package com.wsws.moduledomain.recommendation.repo;

import com.wsws.moduledomain.recommendation.UserRecommendation;
import com.wsws.moduledomain.user.vo.UserInterest;

import java.util.List;

public interface UserRecommendationRepository {
    List<UserRecommendation> findTopRecommendations(String userId, List<UserInterest> userInterests, int limit);
}
