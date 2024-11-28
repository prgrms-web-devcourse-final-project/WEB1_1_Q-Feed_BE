package com.wsws.moduleapplication.service.user;

import com.wsws.moduleapplication.dto.user.AuthServiceResponse;
import com.wsws.moduleapplication.dto.auth.LoginServiceRequest;
import com.wsws.moduleapplication.dto.auth.LoginServiceResponse;
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

    //로그인
    public LoginServiceResponse login(LoginServiceRequest loginServiceRequest) {
        Email email = Email.from(loginServiceRequest.email());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다."));

        if(!passwordEncoder.matches(loginServiceRequest.password(),user.getPassword().getValue())){
            throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다.");
        }

        // JWT 토큰 생성
        String accessToken = tokenProvider.createAccessToken(user.getId().getValue());
        String refreshToken = tokenProvider.createRefreshToken(user.getId().getValue());

        //refreshToken은 7일
        authRepository.save(RefreshToken.create(refreshToken, LocalDateTime.now().plusDays(7)));

        return new LoginServiceResponse(accessToken, refreshToken);
    }


    //로그아웃
    public AuthServiceResponse logout(String refreshToken) {
        authRepository.deleteByToken(refreshToken);
        return new AuthServiceResponse("로그아웃이 완료되었습니다");
    }



    //토큰 재발급
    public LoginServiceResponse reissueToken(String refreshToken){
        //token 있는지 확인
        RefreshToken rToken = authRepository.findByToken(refreshToken)
                .orElseThrow(() -> new InvalidRefreshTokenException("유효하지 않은 리프레시 토큰입니다."));

        //유효기간 체크
        rToken.validateExpiry();

        String newAccessToken = tokenProvider.createAccessToken(rToken.getToken());
        String newRefreshToken = tokenProvider.createRefreshToken(rToken.getToken());

        authRepository.deleteByToken(refreshToken);
        authRepository.save(RefreshToken.create(newRefreshToken, LocalDateTime.now().plusDays(7)));

        return new LoginServiceResponse(newAccessToken, newRefreshToken);
    }
}
