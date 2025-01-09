package com.wsws.moduledomain.socialnetwork.recommendation;

import java.util.List;

public interface UserRecommendationRepository {
    List<Recommendation> findTopRecommendations(String userId, List<Long> interestCategoryIds, int limit);
    List<Recommendation> findGeneralRecommendations(String userId, int limit);
}
