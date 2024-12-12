package com.wsws.moduleapplication.socialnetwork.recommendation;

import com.wsws.moduleapplication.socialnetwork.recommendation.mapper.UserRecommendationMapper;
import com.wsws.moduleapplication.socialnetwork.recommendation.dto.UserRecommendationResponse;
import com.wsws.moduledomain.socialnetwork.follow.vo.UserRecommendation;
import com.wsws.moduledomain.socialnetwork.follow.UserRecommendationRepository;
import com.wsws.moduledomain.socialnetwork.interest.UserInterestRepository;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final UserRecommendationRepository userRecommendationRepository;
    private final UserInterestRepository userInterestRepository;

    public List<UserRecommendationResponse> getRecommendations(String userId, int limit) {
        // 사용자 관심사 조회
        List<Long> interestCategoryIds = userInterestRepository.findByUserId(UserId.of(userId))
                .stream()
                .map(interest -> interest.getCategoryId().getValue())
                .toList();

        // 관심사 기반 추천 조회
        List<UserRecommendation> commonInterestUsers = userRecommendationRepository.findTopRecommendations(userId, interestCategoryIds, limit);

        // 부족한 수 일반 사용자로 채우기
        int remaining = limit - commonInterestUsers.size();
        if (remaining > 0) {
            List<UserRecommendation> generalUsers = userRecommendationRepository.findGeneralRecommendations(userId, remaining);
            commonInterestUsers.addAll(generalUsers);
        }


        return UserRecommendationMapper.toDtoList(commonInterestUsers);
    }
}
