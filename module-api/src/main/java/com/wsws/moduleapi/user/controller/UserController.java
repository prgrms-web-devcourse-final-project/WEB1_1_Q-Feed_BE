package com.wsws.moduleapi.user.controller;

import com.wsws.moduleapi.auth.dto.AuthResponse;
import com.wsws.moduleapi.user.dto.UserProfileApiResponse;
import com.wsws.moduleapplication.user.dto.PasswordChangeServiceDto;
import com.wsws.moduleapplication.user.dto.RegisterUserRequest;
import com.wsws.moduleapplication.user.dto.UpdateProfileServiceDto;
import com.wsws.moduleapplication.user.service.UserQueryService;
import com.wsws.moduleapplication.user.service.UserService;
import com.wsws.modulesecurity.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserQueryService userQueryService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody RegisterUserRequest request) {
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse("회원가입이 완료되었습니다."));
    }

    // 사용자 정보 검색
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileApiResponse> getUser(@PathVariable String userId) {
        UserProfileApiResponse response = new UserProfileApiResponse(userQueryService.getUserProfile(userId)) ;
        return ResponseEntity.ok(response);
    }

    // 비밀번호 변경
    @PatchMapping("/password")
    public ResponseEntity<AuthResponse> changePassword(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody PasswordChangeServiceDto request) {
        String userId = userPrincipal.getId();
        userService.changePassword(request, userId);
        return ResponseEntity.ok(new AuthResponse("비밀번호가 변경되었습니다."));
    }

    //사용자 정보 업데이트
    @PatchMapping
    public ResponseEntity<AuthResponse> updateProfile(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody UpdateProfileServiceDto dto) {
        String userId = userPrincipal.getId();
        userService.updateProfile(dto,userId);
        return ResponseEntity.ok(new AuthResponse("사용자 정보 수정이 완료되었습니다."));
    }

    // 회원 정보 삭제
    @DeleteMapping
    public ResponseEntity<AuthResponse> deleteUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        String userId = userPrincipal.getId();
        userService.deleteUser(userId);
        return ResponseEntity.ok(new AuthResponse("회원 탈퇴가 완료되었습니다."));
    }

    //관심사 생성

    @PostMapping("/{userId}/interests")
    public ResponseEntity<AuthResponse> createInterests(
            @PathVariable String userId,
            @RequestBody List<String> interestCategoryNames
    ){
        userService.createInterests(userId,interestCategoryNames);

        return ResponseEntity.ok(new AuthResponse("사용자 관심사가 생성되었습니다."));
    }

    //관심사 수정 및 추가
    @PutMapping("/interests")
    public ResponseEntity<AuthResponse> updateUserInterests(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody List<String> interestCategoryNames){
        String userId = userPrincipal.getId();
        userService.updateInterests(userId, interestCategoryNames);
        return ResponseEntity.ok(new AuthResponse("사용자 관심사가 업데이트되었습니다."));
    }

    // 사용자 관심사 조회
    @GetMapping("/{userId}/interests")
    public ResponseEntity<List<String>> getUserInterests(@PathVariable String userId) {
        List<String> interests = userService.getUserInterests(userId);
        return ResponseEntity.ok(interests);
    }

    // 과거 질문 및 답변 리스트 조회


    // 참여 중인 그룹 목록 조회

}
