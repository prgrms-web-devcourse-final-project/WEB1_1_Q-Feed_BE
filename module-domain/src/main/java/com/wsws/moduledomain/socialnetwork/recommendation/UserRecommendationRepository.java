package com.wsws.moduledomain.socialnetwork.recommendation;

import java.util.List;

public interface UserRecommendationRepository {
    List<UserRecommendation> findTopRecommendations(String userId, List<Long> interestCategoryIds, int limit);
    List<UserRecommendation> findGeneralRecommendations(String userId, int limit);
}
