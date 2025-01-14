package com.wsws.moduleapplication.authcontext.service;

import com.wsws.moduleapplication.authcontext.dto.*;
import com.wsws.moduleapplication.authcontext.exception.EmailNotFoundException;
import com.wsws.moduleapplication.authcontext.exception.RefreshTokenExpiredException;
import com.wsws.moduledomain.authcontext.auth.ParsedTokenInfo;
import com.wsws.moduledomain.authcontext.auth.RefreshToken;
import com.wsws.moduledomain.authcontext.auth.repo.AuthRepository;
import com.wsws.moduledomain.authcontext.auth.repo.TokenProvider;
import com.wsws.moduledomain.authcontext.auth.repo.VerificationCodeStore;
import com.wsws.moduledomain.usercontext.user.aggregate.User;
import com.wsws.moduledomain.usercontext.user.encoder.PasswordEncoder;
import com.wsws.moduledomain.usercontext.user.repo.UserRepository;
import com.wsws.moduledomain.usercontext.user.vo.Email;
import com.wsws.moduledomain.usercontext.user.vo.Password;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import com.wsws.moduledomain.usercontext.user.vo.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthRepository authRepository;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private AuthService authService;

    @Nested
    @DisplayName("login 메서드는")
    class DescribeLogin{

        @Test
        @DisplayName("이메일과 비밀번호를 전달받아서 JWT토큰을 반환할 수 있다")
        void itLogin(){
            //given
            String email = "tester1234@gmail.com";
            String password = "Tester1234!";
            String accessToken = "access-token";
            String refreshToken = "refresh-token";
            String userId = "userId";
            User user = mock(User.class);
            Password userPassword = mock(Password.class);


            when(userRepository.findByEmail(Email.from(email))).thenReturn(Optional.of(user));
            when(user.getId()).thenReturn(UserId.of(userId));
            when(user.getPassword()).thenReturn(userPassword);
            when(user.getUserRole()).thenReturn(UserRole.ROLE_USER);
            when(tokenProvider.createAccessToken(anyString(), anyString())).thenReturn(accessToken);
            when(tokenProvider.createRefreshToken(anyString(), anyString())).thenReturn(refreshToken);


            LoginServiceRequest loginServiceRequest = new LoginServiceRequest(email, password);

            //when
            LoginServiceResponse loginServiceResponse = authService.login(loginServiceRequest);

            //then
            assertThat(loginServiceResponse).isNotNull();
            assertThat(loginServiceResponse.accessToken()).isEqualTo(accessToken);
            assertThat(loginServiceResponse.refreshToken()).isEqualTo(refreshToken);
            verify(authRepository, times(1)).save(any(RefreshToken.class));




        }

        @Test
        @DisplayName("존재하지 않는 이메일을 전달받으면 EmailNotFoundException을 던진다.")
        void itThrowsEmailNotFoundException() {
            //given
            String email = "test1234@gmail.com";
            when(userRepository.findByEmail(Email.from(email))).thenReturn(Optional.empty());

            LoginServiceRequest request = new LoginServiceRequest(email, "password");

            //when
            //then
            assertThatThrownBy(() -> authService.login(request))
                    .isInstanceOf(EmailNotFoundException.class);
        }

    }

    @Nested
    @DisplayName("logout 메서드는")
    class DescribeLogout {

        @Test
        @DisplayName("RefreshToken을 삭제한다")
        void itDeletesRefreshToken() {
            //given
            String refreshToken = "refresh-token";

            //when
            AuthServiceResponse response = authService.logout(refreshToken);

            //then
            assertThat(response.message()).isEqualTo("로그아웃이 완료되었습니다");
            verify(authRepository, times(1)).deleteByToken(refreshToken);
        }
    }

    @Nested
    @DisplayName("reissueToken 메서드는")
    class DescribeReissueToken {

        @Test
        @DisplayName("유효한 RefreshToken으로 새로운 토큰을 발급한다")
        void itReissuesTokens() {
            // given
            String oldRefreshToken = "old-refresh-token";
            String newAccessToken = "new-access-token";
            String newRefreshToken = "new-refresh-token";
            RefreshToken refreshToken = mock(RefreshToken.class);
            ParsedTokenInfo parsedTokenInfo = new ParsedTokenInfo("user1", "USER");

            when(authRepository.findByToken(oldRefreshToken)).thenReturn(Optional.of(refreshToken));
            when(tokenProvider.parseToken(oldRefreshToken)).thenReturn(parsedTokenInfo);
            when(tokenProvider.createAccessToken(anyString(), anyString())).thenReturn(newAccessToken);
            when(tokenProvider.createRefreshToken(anyString(), anyString())).thenReturn(newRefreshToken);

            // when
            TokenReissueAppDto response = authService.reissueToken(oldRefreshToken);

            // then
            assertThat(response.accessToken()).isEqualTo(newAccessToken);
            assertThat(response.refreshToken()).isEqualTo(newRefreshToken);
            verify(authRepository, times(1)).deleteByToken(oldRefreshToken);
            verify(authRepository, times(1)).save(any(RefreshToken.class));
        }

        @Test
        @DisplayName("만료된 RefreshToken이면 RefreshTokenExpiredException을 던진다")
        void itThrowsRefreshTokenExpiredException() {
            // given
            String oldRefreshToken = "expired-refresh-token";
            RefreshToken refreshToken = mock(RefreshToken.class);

            when(authRepository.findByToken(oldRefreshToken)).thenReturn(Optional.of(refreshToken));
            doThrow(RefreshTokenExpiredException.EXCEPTION).when(refreshToken).validateExpiry();

            // when & then
            assertThatThrownBy(() -> authService.reissueToken(oldRefreshToken))
                    .isInstanceOf(RefreshTokenExpiredException.class);
        }
    }







}