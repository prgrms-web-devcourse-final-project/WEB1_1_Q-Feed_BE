package com.wsws.moduleinfra.repo.user;

import com.wsws.moduledomain.user.User;
import com.wsws.moduledomain.user.repo.SocialLoginRepository;
import com.wsws.moduledomain.user.vo.SocialLoginInfo;
import com.wsws.moduleinfra.entity.user.SocialLoginEntity;
import com.wsws.moduleinfra.entity.user.UserEntity;
import com.wsws.moduleinfra.entity.user.UserEntityMapper;
import com.wsws.moduleinfra.repo.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SocialLoginRepositoryImpl implements SocialLoginRepository {

    private final JpaSocialLoginRepository jpaRepository;
    private final JpaUserRepository jpaUserRepository;


    @Override
    public Optional<SocialLoginInfo> findByProviderAndProviderId(String provider, String providerId) {
        return jpaRepository.findByProviderAndProviderId(provider, providerId)
                .map(this::toSocialLoginInfo);
    }

    @Override
    public void save(String provider, String providerId, User user) {
        SocialLoginEntity entity = SocialLoginEntity.create(
                provider,
                providerId,
                UserEntityMapper.fromDomain(user)
        );
        jpaRepository.save(entity);
    }

    @Override
    public boolean existsByProviderAndProviderId(String provider, String providerId) {
        return jpaRepository.existsByProviderAndProviderId(provider, providerId);
    }

    private SocialLoginInfo toSocialLoginInfo(SocialLoginEntity entity) {
        return new SocialLoginInfo(
                entity.getProvider(),
                entity.getProviderId(),
                entity.getUser().getEmail(),
                entity.getUser().getNickname(),
                entity.getUser().getProfileImage()
        );
    }
}
