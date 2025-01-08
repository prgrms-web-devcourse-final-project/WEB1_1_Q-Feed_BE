package com.wsws.moduledomain.authcontext.social.aggregate;

import com.wsws.moduledomain.authcontext.social.exception.InvalidProviderException;
import com.wsws.moduledomain.authcontext.social.exception.InvalidProviderIdException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class SocialLoginTest {

    @Test
    void 정상적으로_SocialLogin을_생성할_수_있다() {
        // given
        String provider = "kakao";
        String providerId = "123456";
        String email = "test@kakao.com";
        String nickname = "kakaoUser";
        String profileImageUrl = "http://kakao.com/profile.jpg";

        // when
        SocialLogin socialLogin = SocialLogin.create(
                provider, providerId, email, nickname, profileImageUrl
        );

        // then
        assertThat(socialLogin).isNotNull();
        assertThat(socialLogin.getProvider()).isEqualTo(provider);
        assertThat(socialLogin.getProviderId()).isEqualTo(providerId);
        assertThat(socialLogin.getEmail()).isEqualTo(email);
        assertThat(socialLogin.getNickname()).isEqualTo(nickname);
        assertThat(socialLogin.getProfileImageUrl()).isEqualTo(profileImageUrl);
    }

    @Test
    void provider가_null이면_InvalidProviderException_발생() {
        // given
        String nullProvider = null;
        String providerId = "k123";
        String email = "test@kakao.com";
        String nickname = "kakaoUser";
        String profileImageUrl = "http://kakao.com/profile.jpg";

        // when & then
        assertThatThrownBy(() ->
                SocialLogin.create(nullProvider, providerId, email, nickname, profileImageUrl)
        ).isInstanceOf(InvalidProviderException.class);
    }

    @Test
    void provider가_빈문자열이면_InvalidProviderException_발생() {
        // given
        String emptyProvider = "";
        String providerId = "k123";
        String email = "test@kakao.com";
        String nickname = "kakaoUser";
        String profileImageUrl = "http://kakao.com/profile.jpg";

        // when & then
        assertThatThrownBy(() ->
                SocialLogin.create(emptyProvider, providerId, email, nickname, profileImageUrl)
        ).isInstanceOf(InvalidProviderException.class);
    }

    @Test
    void providerId가_null이면_InvalidProviderIdException_발생() {
        // given
        String provider = "kakao";
        String nullProviderId = null;
        String email = "test@kakao.com";
        String nickname = "kakaoUser";
        String profileImageUrl = "http://kakao.com/profile.jpg";

        // when & then
        assertThatThrownBy(() ->
                SocialLogin.create(provider, nullProviderId, email, nickname, profileImageUrl)
        ).isInstanceOf(InvalidProviderIdException.class);
    }

    @Test
    void providerId가_빈문자열이면_InvalidProviderIdException_발생() {
        // given
        String provider = "kakao";
        String emptyProviderId = "";
        String email = "test@kakao.com";
        String nickname = "kakaoUser";
        String profileImageUrl = "http://kakao.com/profile.jpg";

        // when & then
        assertThatThrownBy(() ->
                SocialLogin.create(provider, emptyProviderId, email, nickname, profileImageUrl)
        ).isInstanceOf(InvalidProviderIdException.class);
    }

}