package com.wsws.moduleapi.controller.user;

import com.wsws.moduleapi.dto.user.*;
import com.wsws.moduleapplication.dto.user.PasswordChangeServiceDto;
import com.wsws.moduleapplication.dto.user.RegisterUserRequest;
import com.wsws.moduleapplication.dto.user.UpdateProfileServiceDto;
import com.wsws.moduleapplication.dto.user.UserProfileResponse;
import com.wsws.moduleapplication.service.user.UserQueryService;
import com.wsws.moduleapplication.service.user.UserService;
import com.wsws.modulesecurity.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserQueryService userQueryService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody RegisterUserRequest request) {
        userService.register(request);
        return ResponseEntity.ok().build();
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
    public ResponseEntity<Void> updateProfile(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody UpdateProfileServiceDto dto) {
        String userId = userPrincipal.getId();
        userService.updateProfile(dto,userId);
        return ResponseEntity.ok().build();
    }

    // 회원 정보 삭제
    @DeleteMapping
    public ResponseEntity<AuthResponse> deleteUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        String userId = userPrincipal.getId();
        userService.deleteUser(userId);
        return ResponseEntity.ok(new AuthResponse("회원 정보가 삭제되었습니다."));
    }

    // 과거 질문 및 답변 리스트 조회


    // 참여 중인 그룹 목록 조회

}
