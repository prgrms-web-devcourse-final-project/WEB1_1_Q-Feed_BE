package com.wsws.moduleapi.user.controller;

import com.wsws.moduleapi.auth.dto.AuthResponse;
import com.wsws.moduleapi.user.dto.UserProfileApiResponse;
import com.wsws.moduleapplication.user.dto.PasswordChangeServiceDto;
import com.wsws.moduleapplication.user.dto.RegisterUserRequest;
import com.wsws.moduleapplication.user.dto.UpdateFcmTokenRequest;
import com.wsws.moduleapplication.user.dto.UpdateProfileServiceDto;
import com.wsws.moduleapplication.user.service.UserQueryService;
import com.wsws.moduleapplication.user.service.UserService;
import com.wsws.modulesecurity.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth") // Security 적용
public class UserController {

    private final UserService userService;
    private final UserQueryService userQueryService;

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody RegisterUserRequest request) {
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse("회원가입이 완료되었습니다."));
    }

    @Operation(summary = "사용자 정보 검색", description = "주어진 사용자 ID로 사용자 정보를 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 검색 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content)
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileApiResponse> getUser(
            @Parameter(description = "검색할 사용자의 ID") @PathVariable String userId) {
        UserProfileApiResponse response = new UserProfileApiResponse(userQueryService.getUserProfile(userId));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "비밀번호 변경", description = "현재 인증된 사용자의 비밀번호를 변경합니다.")
    @PatchMapping("/password")
    public ResponseEntity<AuthResponse> changePassword(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody PasswordChangeServiceDto request) {
        String userId = userPrincipal.getId();
        userService.changePassword(request, userId);
        return ResponseEntity.ok(new AuthResponse("비밀번호가 변경되었습니다."));
    }

    @Operation(summary = "사용자 정보 업데이트", description = "현재 인증된 사용자의 정보를 업데이트합니다.")
    @PatchMapping
    public ResponseEntity<AuthResponse> updateProfile(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody UpdateProfileServiceDto dto) {
        String userId = userPrincipal.getId();
        userService.updateProfile(dto, userId);
        return ResponseEntity.ok(new AuthResponse("사용자 정보 수정이 완료되었습니다."));
    }

    @Operation(summary = "회원 탈퇴", description = "현재 인증된 사용자를 탈퇴 처리합니다.")
    @DeleteMapping
    public ResponseEntity<AuthResponse> deleteUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        String userId = userPrincipal.getId();
        userService.deleteUser(userId);
        return ResponseEntity.ok(new AuthResponse("회원 탈퇴가 완료되었습니다."));
    }

    @Operation(summary = "사용자 관심사 생성", description = "특정 사용자에 대한 관심사를 생성합니다.")
    @PostMapping("/{userId}/interests")
    public ResponseEntity<AuthResponse> createInterests(
            @Parameter(description = "관심사를 생성할 사용자의 ID") @PathVariable String userId,
            @RequestBody List<String> interestCategoryNames) {
        userService.createInterests(userId, interestCategoryNames);
        return ResponseEntity.ok(new AuthResponse("사용자 관심사가 생성되었습니다."));
    }

    @Operation(summary = "사용자 관심사 수정 및 추가", description = "현재 인증된 사용자의 관심사를 수정 또는 추가합니다.")
    @PutMapping("/interests")
    public ResponseEntity<AuthResponse> updateUserInterests(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody List<String> interestCategoryNames) {
        String userId = userPrincipal.getId();
        userService.updateInterests(userId, interestCategoryNames);
        return ResponseEntity.ok(new AuthResponse("사용자 관심사가 업데이트되었습니다."));
    }

    @Operation(summary = "사용자 관심사 조회", description = "특정 사용자의 관심사를 조회합니다.")
    @GetMapping("/{userId}/interests")
    public ResponseEntity<List<String>> getUserInterests(
            @Parameter(description = "관심사를 조회할 사용자의 ID") @PathVariable String userId) {
        List<String> interests = userService.getUserInterests(userId);
        return ResponseEntity.ok(interests);
    }

    @PostMapping("/fcmTokenSaves")
    public ResponseEntity<String> saveFcmToken(
            UpdateFcmTokenRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ){
        String userId = userPrincipal.getId();
        userService.saveFcmToken(request, userId);
        return ResponseEntity.ok("FCM 토큰 저장 OK");
    }
}
