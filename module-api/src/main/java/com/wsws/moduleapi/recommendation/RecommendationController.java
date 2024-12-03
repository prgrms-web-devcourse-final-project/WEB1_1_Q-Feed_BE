package com.wsws.moduleapi.recommendation;

import com.wsws.moduleapplication.user.dto.UserRecommendationResponse;
import com.wsws.moduleapplication.user.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
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
