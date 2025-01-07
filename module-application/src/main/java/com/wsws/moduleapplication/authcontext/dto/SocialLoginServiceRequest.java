package com.wsws.moduleapplication.authcontext.dto;

public record SocialLoginServiceRequest(
        String authorizationCode,
        String fcmToken
) {
}
