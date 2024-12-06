package com.wsws.moduledomain.user.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SocialLoginInfo {
    private final String provider; // "kakao"
    private final String providerId;
    private final String email;
    private final String nickname;
    private final String profileImageUrl;
}
