package com.wsws.moduleinfra.authcontext.social.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "social_logins")
public class SocialLoginEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String provider; // "kakao"

    @Column(nullable = false)
    private String providerId; // 카카오 사용자 고유 ID

    private String userId;

    public static SocialLoginEntity create(String provider, String providerId,String userId ) {
        SocialLoginEntity socialLogin = new SocialLoginEntity();
        socialLogin.provider = provider;
        socialLogin.providerId = providerId;
        socialLogin.userId = userId;
        return socialLogin;
    }
}
