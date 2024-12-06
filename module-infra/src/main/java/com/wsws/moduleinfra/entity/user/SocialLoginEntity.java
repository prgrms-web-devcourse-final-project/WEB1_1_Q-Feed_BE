package com.wsws.moduleinfra.entity.user;

import com.wsws.moduledomain.user.User;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public static SocialLoginEntity create(String provider, String providerId, UserEntity user) {
        SocialLoginEntity socialLogin = new SocialLoginEntity();
        socialLogin.provider = provider;
        socialLogin.providerId = providerId;
        socialLogin.user = user;
        return socialLogin;
    }

    public static SocialLoginEntity createWithUser(String provider, String providerId, User user){
        SocialLoginEntity socialLogin = new SocialLoginEntity();
        socialLogin.provider = provider;
        socialLogin.providerId = providerId;
        socialLogin.user = UserEntityMapper.fromDomain(user);

        return socialLogin;
    }
}
