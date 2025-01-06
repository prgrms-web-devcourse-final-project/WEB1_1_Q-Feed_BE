package com.wsws.moduledomain.authcontext.social.aggregate;

import com.wsws.moduledomain.authcontext.social.exception.InvalidProviderException;
import com.wsws.moduledomain.authcontext.social.exception.InvalidProviderIdException;
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
        if(provider == null || provider.isEmpty()) {
            throw InvalidProviderException.EXCEPTION;
        }

        if(providerId == null || providerId.isEmpty()) {
            throw InvalidProviderIdException.EXCEPTION;
        }
        return new SocialLogin(provider, providerId, email, nickname, profileImageUrl);
    }

    private SocialLogin(String provider, String providerId, String email, String nickname, String profileImageUrl) {
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }


}
