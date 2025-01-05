package com.wsws.moduleapi.socialnetwork.userInterest;

import com.wsws.moduleapi.auth.dto.AuthResponse;
import com.wsws.moduleapplication.socialnetwork.interest.service.UserInterestService;
import com.wsws.modulesecurity.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserInterestController {

    private final UserInterestService userInterestService;

    @Operation(summary = "사용자 관심사 생성", description = "특정 사용자에 대한 관심사를 생성합니다.")
    @PostMapping("/{userId}/interests")
    public ResponseEntity<AuthResponse> createInterests(
            @Parameter(description = "관심사를 생성할 사용자의 ID") @PathVariable String userId,
            @RequestBody List<String> interestCategoryNames) {
        userInterestService.createInterests(userId, interestCategoryNames);
        return ResponseEntity.ok(new AuthResponse("사용자 관심사가 생성되었습니다."));
    }

    @Operation(summary = "사용자 관심사 수정 및 추가", description = "현재 인증된 사용자의 관심사를 수정 또는 추가합니다.")
    @PutMapping("/interests")
    public ResponseEntity<AuthResponse> updateUserInterests(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody List<String> interestCategoryNames) {
        String userId = userPrincipal.getId();
        userInterestService.updateInterests(userId, interestCategoryNames);
        return ResponseEntity.ok(new AuthResponse("사용자 관심사가 업데이트되었습니다."));
    }

    @Operation(summary = "사용자 관심사 조회", description = "특정 사용자의 관심사를 조회합니다.")
    @GetMapping("/{userId}/interests")
    public ResponseEntity<List<String>> getUserInterests(
            @Parameter(description = "관심사를 조회할 사용자의 ID") @PathVariable String userId) {
        List<String> interests = userInterestService.getUserInterests(userId);
        return ResponseEntity.ok(interests);
    }
}