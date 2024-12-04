package com.wsws.moduleapplication.user.service;

import com.wsws.moduleapplication.user.dto.UserRecommendationMapper;
import com.wsws.moduleapplication.user.dto.UserRecommendationResponse;
import com.wsws.moduledomain.recommendation.repo.UserRecommendationRepository;
import com.wsws.moduledomain.user.repo.UserInterestRepository;
import com.wsws.moduledomain.user.vo.UserId;
import com.wsws.moduledomain.user.vo.UserInterest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final UserRecommendationRepository userRecommendationRepository;
    private final UserInterestRepository userInterestRepository;

    public List<UserRecommendationResponse> getRecommendations(String userId, int limit) {

        List<UserInterest> userInterests = userInterestRepository.findByUserId(UserId.of(userId));
        return UserRecommendationMapper.toDtoList(userRecommendationRepository.findTopRecommendations(userId, userInterests, limit)) ;
    }
}
