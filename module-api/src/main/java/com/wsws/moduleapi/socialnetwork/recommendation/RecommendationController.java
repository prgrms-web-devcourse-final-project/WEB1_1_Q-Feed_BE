package com.wsws.moduleapi.socialnetwork.recommendation;

import com.wsws.moduleapplication.socialnetwork.recommendation.dto.UserRecommendationResponse;
import com.wsws.moduleapplication.socialnetwork.recommendation.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/{userId}")
    public List<UserRecommendationResponse> getUserRecommendations(
            @PathVariable String userId,
            @RequestParam(defaultValue = "10") int limit) {

        return recommendationService.getRecommendations(userId, limit);
    }
}
