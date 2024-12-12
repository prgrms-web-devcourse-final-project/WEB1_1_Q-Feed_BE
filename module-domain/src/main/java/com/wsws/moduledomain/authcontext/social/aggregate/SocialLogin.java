package com.wsws.moduledomain.authcontext.social.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter

public class SocialLogin {
    private Long id;
    private final String provider; // "kakao"
    private final String providerId;
    private final String email;
    private final String nickname;
    private final String profileImageUrl;

    public static SocialLogin create(String provider, String providerId, String email, String nickname, String profileImageUrl) {
        return new SocialLogin(provider, providerId, email, nickname, profileImageUrl);
    }

    private SocialLogin(String provider, String providerId, String email, String nickname, String profileImageUrl) {
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    protected void setId(Long id) {
        this.id = id;
    }

}
