package com.wsws.moduledomain.authcontext.auth;

import com.wsws.moduledomain.authcontext.exception.ExpiredRefreshTokenException;
import com.wsws.moduledomain.authcontext.exception.InvalidRefreshTokenException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class RefreshTokenTest {

    @Test
    void 유효한_RefreshToken을_생성할_수_있다() {
        // given
        String token = "validToken123";
        LocalDateTime expiryDate = LocalDateTime.now().plusDays(1);

        // when
        RefreshToken refreshToken = RefreshToken.create(token, expiryDate);

        // then
        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken.getToken()).isEqualTo(token);
        assertThat(refreshToken.getExpiryDate()).isEqualTo(expiryDate);
    }

    @Test
    void 토큰이_null이거나_빈문자열이면_InvalidRefreshTokenException_발생() {
        // given
        String invalidToken = "";
        LocalDateTime expiryDate = LocalDateTime.now().plusDays(1);

        // when & then
        assertThatThrownBy(() -> RefreshToken.create(invalidToken, expiryDate))
                .isInstanceOf(InvalidRefreshTokenException.class);
    }

    @Test
    void 만료시간이_null이면_ExpiredRefreshTokenException_발생() {
        // given
        String token = "validToken123";
        LocalDateTime expiryDate = null;

        // when & then
        assertThatThrownBy(() -> RefreshToken.create(token, expiryDate))
                .isInstanceOf(ExpiredRefreshTokenException.class);
    }

    @Test
    void 만료시간이_현재보다_이전이면_ExpiredRefreshTokenException_발생() {
        // given
        String token = "validToken123";
        LocalDateTime pastExpiryDate = LocalDateTime.now().minusDays(1);

        // when & then
        assertThatThrownBy(() -> RefreshToken.create(token, pastExpiryDate))
                .isInstanceOf(ExpiredRefreshTokenException.class);
    }

    @Test
    void 만료된_토큰은_create_메서드에서_ExpiredRefreshTokenException_발생() {
        // given
        String token = "validToken123";
        LocalDateTime expiryDate = LocalDateTime.now().minusHours(1); // 이미 만료됨

        // when & then
        assertThatThrownBy(() -> RefreshToken.create(token, expiryDate))
                .isInstanceOf(ExpiredRefreshTokenException.class);
    }

    @Test
    void 유효한_토큰은_validateExpiry_메서드에서_예외가_발생하지_않는다() {
        // given
        String token = "validToken123";
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(1); // 아직 유효함
        RefreshToken refreshToken = RefreshToken.create(token, expiryDate);

        // when & then
        assertThatCode(refreshToken::validateExpiry)
                .doesNotThrowAnyException();
    }
}