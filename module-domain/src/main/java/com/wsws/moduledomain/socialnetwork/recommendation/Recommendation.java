package com.wsws.moduledomain.socialnetwork.recommendation;

import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
public class Recommendation {
    private final String userId;
    private final List<UserRecommendation> userRecommendations;

    // 정렬 및 제한된 결과 반환
    public Recommendation sortAndLimitRecommendations(int limit) {
        List<UserRecommendation> sortedRecommendations = this.userRecommendations.stream()
                .sorted(Comparator.comparingLong(UserRecommendation::getFollowerCount).reversed())
                .limit(limit)
                .toList();
        return new Recommendation(this.userId, sortedRecommendations);
    }

    public List<UserRecommendation> getRecommendations() {
        return Collections.unmodifiableList(userRecommendations); // 방어적 반환
    }



}
