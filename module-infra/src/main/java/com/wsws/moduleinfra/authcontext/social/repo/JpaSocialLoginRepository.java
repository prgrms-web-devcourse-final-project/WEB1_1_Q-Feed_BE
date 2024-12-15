package com.wsws.moduleinfra.authcontext.social.repo;

import com.wsws.moduleinfra.authcontext.social.entity.SocialLoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaSocialLoginRepository extends JpaRepository<SocialLoginEntity, Long> {
    Optional<SocialLoginEntity> findByProviderAndProviderId(String provider, String providerId);
    boolean existsByProviderAndProviderId(String provider, String providerId);
}
