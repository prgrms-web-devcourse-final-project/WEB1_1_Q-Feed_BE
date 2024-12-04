package com.wsws.moduleapplication.auth.service;

import com.wsws.moduleapplication.auth.dto.*;
import com.wsws.moduleapplication.auth.exception.EmailNotFoundException;
import com.wsws.moduleapplication.auth.exception.InvalidVerificationCodeException;
import com.wsws.moduleapplication.auth.exception.RefreshTokenExpiredException;
import com.wsws.moduleapplication.user.dto.AuthServiceResponse;
import com.wsws.moduledomain.auth.repo.EmailService;
import com.wsws.moduledomain.auth.RefreshToken;
import com.wsws.moduledomain.auth.repo.TokenProvider;
import com.wsws.moduledomain.auth.exception.InvalidRefreshTokenException;
import com.wsws.moduledomain.auth.repo.AuthRepository;
import com.wsws.moduledomain.auth.repo.VerificationCodeStore;
import com.wsws.moduledomain.user.PasswordEncoder;
import com.wsws.moduledomain.user.User;
import com.wsws.moduledomain.user.repo.UserRepository;
import com.wsws.moduledomain.user.vo.Email;
import com.wsws.moduledomain.user.vo.Nickname;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final VerificationCodeStore codeStore;
    private final EmailService emailService;

    //인증 코드 유효 시간
    private static final long CODE_TTL = 300; //5분
    private static final String VERIFICATION_CODE_PREFIX = "verificationCode:";
    private static final String PASSWORD_RESET_CODE_PREFIX = "passwordResetCode:";


    // 로그인
    public LoginServiceResponse login(LoginServiceRequest loginServiceRequest) {
        Email email = Email.from(loginServiceRequest.email());
        User user = findUserByEmail(email.getValue());

        // 비밀번호 검증
        user.getPassword().matches(loginServiceRequest.password(), passwordEncoder);

        // JWT 토큰 생성
        String accessToken = tokenProvider.createAccessToken(user.getId().getValue());
        String refreshToken = tokenProvider.createRefreshToken(user.getId().getValue());

        // RefreshToken 저장 (7일 유효)
        authRepository.save(RefreshToken.create(refreshToken, LocalDateTime.now().plusDays(7)));

        return new LoginServiceResponse(accessToken, refreshToken);
    }

    // 로그아웃
    public AuthServiceResponse logout(String refreshToken) {
        authRepository.deleteByToken(refreshToken);
        return new AuthServiceResponse("로그아웃이 완료되었습니다");
    }

    // 토큰 재발급
    public LoginServiceResponse reissueToken(String refreshToken) {
        // RefreshToken 확인
        RefreshToken rToken = authRepository.findByToken(refreshToken)
                .orElseThrow(() -> InvalidRefreshTokenException.EXCEPTION);

        // 유효기간 체크
        try {
            rToken.validateExpiry();
        } catch (Exception e) {
            throw RefreshTokenExpiredException.EXCEPTION;
        }

        String newAccessToken = tokenProvider.createAccessToken(rToken.getToken());
        String newRefreshToken = tokenProvider.createRefreshToken(rToken.getToken());

        authRepository.deleteByToken(refreshToken);
        authRepository.save(RefreshToken.create(newRefreshToken, LocalDateTime.now().plusDays(7)));

        return new LoginServiceResponse(newAccessToken, newRefreshToken);
    }

    // 이메일 인증 코드 발송
    public void sendVerificationCode(EmailVerificationServiceDto dto) {
        // 이메일 인증 코드 생성 및 저장 로직
        String verificationCode = generateVerificationCode();

        saveVerificationCode(VERIFICATION_CODE_PREFIX, dto.email(), verificationCode, CODE_TTL);

        String subject = "이메일 인증 코드";
        String bodyTemplate = "아래 인증 코드를 입력하여 이메일 인증을 완료하세요.\n\n인증 코드: {code}";

        sendEmailWithCode(dto.email(), subject, bodyTemplate, verificationCode);


    }

    // 이메일 인증 코드 확인
    public void checkVerificationCode(EmailVerificationCheckServiceDto dto) {
        verifyCodeAndDelete(VERIFICATION_CODE_PREFIX, dto.email(), dto.code());
    }

    // 비밀번호 재설정 요청
    public void sendPasswordResetCode(PasswordResetRequestServiceDto dto) {
        //존재하는 이메일인지 확인
        checkUserExistsByEmail(dto.email());

        String resetCode = generateVerificationCode();

        saveVerificationCode(PASSWORD_RESET_CODE_PREFIX, dto.email(), resetCode, CODE_TTL);

        String subject = "비밀번호 재설정 코드";
        String bodyTemplate = "아래 인증 코드를 입력하여 비밀번호를 재설정하세요.\n\n재설정 코드: {code}";

        sendEmailWithCode(dto.email(), subject, bodyTemplate, resetCode);
    }

    // 비밀번호 재설정
    public void resetPassword(PasswordResetConfirmServiceDto dto) {
        verifyCodeAndDelete(PASSWORD_RESET_CODE_PREFIX, dto.email(), dto.code());

        User user = findUserByEmail(dto.email());

        user.resetPassword(dto.newPassword(), passwordEncoder);


    }

    public boolean checkNickname(String nickname) {
        return userRepository.findByNickname(Nickname.from(nickname)).isPresent();

    }


    //인증번호 생성
    private String generateVerificationCode() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase(); // 랜덤한 6자리 코드 생성
    }

    //인증코드 저장
    private void saveVerificationCode(String keyPrefix, String email, String code, long ttl) {
        String key = keyPrefix + email;
        codeStore.saveCode(key, code, ttl);
    }

    //
    private void verifyCodeAndDelete(String keyPrefix, String email, String inputCode) {
        String key = keyPrefix + email;
        String storedCode = codeStore.getCode(key)
                .orElseThrow(() -> InvalidVerificationCodeException.EXCEPTION);

        if (!storedCode.equals(inputCode)) {
            throw InvalidVerificationCodeException.EXCEPTION;
        }

        codeStore.deleteCode(key);
    }

    private void sendEmailWithCode(String email, String subject, String bodyTemplate, String code) {
        String body = bodyTemplate.replace("{code}", code);
        emailService.sendEmail(email, subject, body);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(Email.from(email))
                .orElseThrow(() -> EmailNotFoundException.EXCEPTION);
    }

    private void checkUserExistsByEmail(String email) {
        if (!userRepository.existsByEmail(Email.from(email))) {
            throw EmailNotFoundException.EXCEPTION;
        }
    }
}
