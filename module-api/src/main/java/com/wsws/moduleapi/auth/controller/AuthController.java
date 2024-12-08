package com.wsws.moduleapi.auth.controller;

import com.wsws.moduleapi.auth.dto.*;
import com.wsws.moduleapplication.auth.dto.LoginServiceResponse;
import com.wsws.moduleapplication.auth.dto.TokenReissueAppDto;
import com.wsws.moduleapplication.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인", description = "사용자가 이메일과 비밀번호를 이용해 로그인합니다.")
    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginServiceResponse serviceResponse = authService.login(request.toServiceDto());
        return ResponseEntity.ok(new LoginResponse(serviceResponse));
    }
    @Operation(summary = "kakao 로그인", description = "사용자가 카카오 계정을 이용해 로그인합니다.")
    @GetMapping("/login/kakao")
    public LoginServiceResponse kakaoLogin(@RequestParam("code") String authorizationCode) {
        return authService.socialLogin(authorizationCode);
    }

    @Operation(summary = "로그아웃", description = "사용자가 리프레시 토큰을 이용해 로그아웃합니다.")
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(@RequestBody LogoutRequest request) {
        authService.logout(request.refreshToken());
        return ResponseEntity.ok(new AuthResponse("로그아웃이 완료되었습니다"));
    }

    @Operation(summary = "토큰 재발행", description = "리프레시 토큰을 이용해 새로운 엑세스 토큰과 리프레시 토큰을 발급받습니다.")
    @PostMapping("/reissue")
    public ResponseEntity<TokenReissueResponse> reissueTokens(@RequestBody TokenReissueRequest request) {
        TokenReissueAppDto serviceResponse = authService.reissueToken(request.refreshToken());
        return ResponseEntity.ok(new TokenReissueResponse(serviceResponse));
    }

    @Operation(summary = "비밀번호 재설정 요청", description = "사용자가 비밀번호 재설정을 위해 인증 코드를 요청합니다.")
    @PostMapping("/reset-password/request")
    public ResponseEntity<AuthResponse> requestPasswordReset(@RequestBody PasswordResetRequest request) {
        authService.sendPasswordResetCode(request.toServiceDto());
        return ResponseEntity.ok(new AuthResponse("비밀번호 재설정 인증번호가 발송되었습니다"));
    }

    @Operation(summary = "비밀번호 재설정 확인", description = "사용자가 비밀번호 재설정을 완료합니다.")
    @PostMapping("/reset-password/confirm")
    public ResponseEntity<AuthResponse> confirmPasswordReset(@RequestBody PasswordResetConfirmRequest request) {
        authService.resetPassword(request.toServiceDto());
        return ResponseEntity.ok(new AuthResponse("비밀번호가 성공적으로 재설정되었습니다"));
    }

    @Operation(summary = "이메일 인증번호 요청", description = "회원가입을 위해 이메일로 인증번호를 요청합니다.")
    @PostMapping("/email/verify")
    public ResponseEntity<AuthResponse> sendEmailVerificationCode(@RequestBody EmailVerificationRequest request) {
        authService.sendVerificationCode(request.toServiceDto());
        return ResponseEntity.ok(new AuthResponse("회원가입 인증번호가 발송되었습니다"));
    }

    @Operation(summary = "이메일 인증번호 확인", description = "회원가입을 위해 이메일로 받은 인증번호를 확인합니다.")
    @PostMapping("/email/verify/check")
    public ResponseEntity<AuthResponse> checkEmailVerificationCode(@RequestBody EmailVerificationCheckRequest request) {
        authService.checkVerificationCode(request.toServiceDto());
        return ResponseEntity.ok(new AuthResponse("인증번호가 확인되었습니다"));
    }

    @Operation(summary = "닉네임 중복 체크", description = "사용 가능한 닉네임인지 확인합니다.")
    @PostMapping("/nickname/check")
    public ResponseEntity<Boolean> checkNickname(@RequestBody String nickname) {
        boolean exists = authService.checkNickname(nickname);
        return ResponseEntity.ok(!exists);
    }
    @Operation(summary = "이메일 중복 체크", description = "사용 가능한 이메일인지 확인합니다.")
    @PostMapping("/email/check")
    public ResponseEntity<Boolean> checkEmail(@RequestBody String email) {
        boolean exists = authService.checkEmail(email);
        return ResponseEntity.ok(!exists);
    }
}