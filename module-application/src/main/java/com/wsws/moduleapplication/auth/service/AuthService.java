package com.wsws.moduleapplication.auth.service;

import com.wsws.moduleapplication.auth.exception.EmailNotFoundException;
import com.wsws.moduleapplication.auth.exception.RefreshTokenExpiredException;
import com.wsws.moduleapplication.user.dto.AuthServiceResponse;
import com.wsws.moduleapplication.auth.dto.LoginServiceRequest;
import com.wsws.moduleapplication.auth.dto.LoginServiceResponse;
import com.wsws.moduledomain.auth.RefreshToken;
import com.wsws.moduledomain.auth.TokenProvider;
import com.wsws.moduledomain.auth.exception.InvalidRefreshTokenException;
import com.wsws.moduledomain.auth.repo.AuthRepository;
import com.wsws.moduledomain.user.PasswordEncoder;
import com.wsws.moduledomain.user.User;
import com.wsws.moduledomain.user.repo.UserRepository;
import com.wsws.moduledomain.user.vo.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    // 로그인
    public LoginServiceResponse login(LoginServiceRequest loginServiceRequest) {
        Email email = Email.from(loginServiceRequest.email());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> EmailNotFoundException.EXCEPTION);

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
}
