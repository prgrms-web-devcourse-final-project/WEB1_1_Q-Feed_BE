package com.wsws.moduleapi.socialnetwork.recommendation;

import com.wsws.moduleapplication.socialnetwork.recommendation.dto.UserRecommendationResponse;
import com.wsws.moduleapplication.socialnetwork.recommendation.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Operation(summary = "팔로우 추천 목록 조회", description = "특정 사용자의 팔로워 추천 목록을 조회합니다.")
    @GetMapping("/{userId}")
    public List<UserRecommendationResponse> getUserRecommendations(
            @PathVariable String userId,
            @RequestParam(defaultValue = "10") int limit) {

        return recommendationService.getRecommendations(userId, limit);
    }
}
