package com.wsws.moduleapi.auth.controller;

import com.wsws.moduleapi.auth.dto.*;
import com.wsws.moduleapplication.auth.dto.LoginServiceResponse;
import com.wsws.moduleapplication.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginServiceResponse serviceResponse = authService.login(request.toServiceDto());
        return ResponseEntity.ok(new LoginResponse(serviceResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(@RequestBody LogoutRequest request) {
        authService.logout(request.refreshToken());
        return ResponseEntity.ok(new AuthResponse("로그아웃이 완료되었습니다"));
    }

    // 토큰 재발행
    @PostMapping("/reissue")
    public ResponseEntity<LoginResponse> reissueTokens(@RequestBody TokenReissueRequest request) {
        LoginServiceResponse serviceResponse = authService.reissueToken(request.refreshToken());
        return ResponseEntity.ok(new LoginResponse(serviceResponse));
    }

//    @PostMapping("/reset-password/request")
//    public ResponseEntity<AuthResponse> requestPasswordReset(@RequestBody PasswordResetRequest request) {
//        authService.sendPasswordResetCode(request.toServiceDto());
//        return ResponseEntity.ok(new AuthResponse("비밀번호 재설정 인증번호가 발송되었습니다"));
//    }
//
//    @PostMapping("/reset-password/confirm")
//    public ResponseEntity<AuthResponse> confirmPasswordReset(@RequestBody PasswordResetConfirmRequest request) {
//        authService.resetPassword(request.toServiceDto());
//        return ResponseEntity.ok(new AuthResponse("비밀번호가 성공적으로 재설정되었습니다"));
//    }
//
//    @PostMapping("/email/verify")
//    public ResponseEntity<AuthResponse> sendEmailVerificationCode(@RequestBody EmailVerificationRequest request) {
//        authService.sendVerificationCode(request.toServiceDto());
//        return ResponseEntity.ok(new AuthResponse("회원가입 인증번호가 발송되었습니다"));
//    }
//
//    @PostMapping("/email/verify/check")
//    public ResponseEntity<AuthResponse> checkEmailVerificationCode(@RequestBody EmailVerificationCheckRequest request) {
//        authService.checkVerificationCode(request.toServiceDto());
//        return ResponseEntity.ok(new AuthResponse("인증번호가 확인되었습니다"));
//    }
//
//    // 닉네임 중복 체크
//    @PostMapping("/nickname/check")
//    public ResponseEntity<Boolean> checkNickname(@RequestBody NicknameCheckRequest request) {
//        boolean exists = authService.checkNickname(authMapper.toServiceDto(request));
//        return ResponseEntity.ok(exists);
//    }

}
