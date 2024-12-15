package com.wsws.moduleapplication.socialnetwork.recommendation;

import com.wsws.moduleapplication.socialnetwork.recommendation.mapper.UserRecommendationMapper;
import com.wsws.moduleapplication.socialnetwork.recommendation.dto.UserRecommendationResponse;
import com.wsws.moduledomain.socialnetwork.follow.repo.FollowReadRepository;
import com.wsws.moduledomain.socialnetwork.recommendation.UserRecommendation;
import com.wsws.moduledomain.socialnetwork.recommendation.UserRecommendationRepository;
import com.wsws.moduledomain.socialnetwork.interest.UserInterestRepository;
import com.wsws.moduledomain.usercontext.user.aggregate.User;
import com.wsws.moduledomain.usercontext.user.repo.UserRepository;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final UserInterestRepository userInterestRepository;
    private final FollowReadRepository followReadRepository;
    private final UserRepository userRepository;


    public List<UserRecommendationResponse> getRecommendations(String userId, int limit) {
        // 사용자 관심사 조회
        List<Long> interestCategoryIds = userInterestRepository.findByUserId(UserId.of(userId))
                .stream()
                .map(interest -> interest.getCategoryId().getValue())
                .toList();

        //관심사 기반 사용자 IO 필터링
        List<String> filteredUserIds = userInterestRepository.findUserIdsByInterestCategories(interestCategoryIds);
        if (filteredUserIds.isEmpty()) {
            return Collections.emptyList();
        }

        //사용자별 팔로워 수 집계
        Map<String, Long> followerCounts = followReadRepository.getFollowerCounts(filteredUserIds);
        //사용자 정보 조회 및 결합
        List<User> users = userRepository.findUsersByIds(filteredUserIds);


        List<UserRecommendation> recommendations = users.stream()
                .map(user -> new UserRecommendation(
                        user.getId().getValue(),
                        user.getNickname().getValue(),
                        user.getProfileImage(),
                        followerCounts.getOrDefault(user.getId().getValue(), 0L)
                ))
                .sorted(Comparator.comparingLong(UserRecommendation::getFollowerCount).reversed())
                .limit(limit)
                .toList();


        return UserRecommendationMapper.toDtoList(recommendations);
    }
}
