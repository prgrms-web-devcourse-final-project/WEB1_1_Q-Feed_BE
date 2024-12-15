package com.wsws.moduleinfra.authcontext.social.repo;

import com.wsws.moduledomain.authcontext.social.SocialLoginRepository;
import com.wsws.moduledomain.authcontext.social.aggregate.SocialLogin;
import com.wsws.moduleinfra.authcontext.social.entity.SocialLoginEntity;
import com.wsws.moduleinfra.usercontext.user.entity.UserEntity;
import com.wsws.moduleinfra.usercontext.user.repo.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SocialLoginRepositoryImpl implements SocialLoginRepository {

    private final JpaSocialLoginRepository jpaRepository;
    private final JpaUserRepository jpaUserRepository;

    @Override
    public void save(String provider, String providerId, String userId) {
        SocialLoginEntity entity = SocialLoginEntity.create(
                provider,
                providerId,
                userId
        );
        jpaRepository.save(entity);
    }

    @Override
    public boolean existsByProviderAndProviderId(String provider, String providerId) {
        return jpaRepository.existsByProviderAndProviderId(provider, providerId);
    }

    private SocialLogin toSocialLoginInfo(SocialLoginEntity entity) {
        String userId = entity.getUserId();

        Optional<UserEntity> user = jpaUserRepository.findById(userId);
        return SocialLogin.create(
                entity.getProvider(),
                entity.getProviderId(),
                user.get().getEmail(),
                user.get().getNickname(),
                user.get().getProfileImage()
                );
    }
}
