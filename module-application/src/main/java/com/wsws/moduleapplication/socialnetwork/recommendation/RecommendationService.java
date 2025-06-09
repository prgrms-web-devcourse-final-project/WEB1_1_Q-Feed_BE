package com.wsws.moduleapplication.socialnetwork.recommendation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.wsws.moduleapplication.socialnetwork.recommendation.mapper.UserRecommendationMapper;
import com.wsws.moduleapplication.socialnetwork.recommendation.dto.UserRecommendationResponse;
import com.wsws.moduledomain.cache.CacheManager;
import com.wsws.moduledomain.socialnetwork.follow.repo.FollowReadRepository;
import com.wsws.moduledomain.socialnetwork.recommendation.Recommendation;
import com.wsws.moduledomain.socialnetwork.interest.UserInterestRepository;
import com.wsws.moduledomain.usercontext.user.aggregate.User;
import com.wsws.moduledomain.usercontext.user.repo.UserRepository;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final UserInterestRepository userInterestRepository;
    private final FollowReadRepository followReadRepository;
    private final UserRepository userRepository;
    private final CacheManager cacheManager;

    public List<UserRecommendationResponse> getRecommendations(String userId, int limit) {
        String cacheKey = "recommendation:" + userId;

        TypeReference<List<UserRecommendationResponse>> typeReference = new TypeReference<List<UserRecommendationResponse>>() {};
        List<UserRecommendationResponse> cachedRecommendations = cacheManager.getJson(cacheKey, typeReference);

        // 캐싱되어 있을 경우에는 해당 캐싱 데이터 반환
        if (cachedRecommendations != null) {
            return cachedRecommendations;
        }
        //없을 경우
        List<UserRecommendationResponse> recommendations = generateRecommendations(userId, limit);

        cacheManager.setJson(cacheKey, recommendations, 10);

        return recommendations;

    }


    public List<UserRecommendationResponse> generateRecommendations(String userId, int limit) {
        // 사용자 관심사 조회
        List<Long> interestCategoryIds = userInterestRepository.findByUserId(UserId.of(userId))
                .stream()
                .map(interest -> interest.getCategoryId().getValue())
                .toList();

        //관심사 기반 사용자 IO 필터링
        List<String> filteredUserIds = userInterestRepository.findUserIdsByInterestCategories(interestCategoryIds, userId);
        if (filteredUserIds.isEmpty()) {
            return Collections.emptyList();
        }

        //사용자별 팔로워 수 집계
        Map<String, Long> followerCounts = followReadRepository.getFollowerCounts(filteredUserIds);
        //사용자 정보 조회 및 결합
        List<User> users = userRepository.findUsersByIds(filteredUserIds);

        //우선 순위 큐로 자료구조 개선
        PriorityQueue<Recommendation> pq = new PriorityQueue<>(
                limit,
                Comparator.comparingLong(Recommendation::getFollowerCount)
        );

        for (User user : users) {
            Recommendation recommendation = Recommendation.of(
                    user.getId().getValue(),
                    user.getNickname().getValue(),
                    user.getProfileImage(),
                    followerCounts.getOrDefault(user.getId().getValue(), 0L)
            );

            if(pq.size() < limit){
                pq.offer(recommendation);
            }else if(recommendation.getFollowerCount() > pq.peek().getFollowerCount()){
                pq.poll();
                pq.offer(recommendation);
            }
        }

        // limit개만큼만 추출
        List<Recommendation> recommendations = new ArrayList<>(pq);
        recommendations.sort(Comparator.comparingLong(Recommendation::getFollowerCount).reversed());



        return UserRecommendationMapper.toDtoList(recommendations);
    }

}