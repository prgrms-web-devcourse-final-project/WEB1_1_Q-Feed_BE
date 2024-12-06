package com.wsws.moduledomain.user.repo;

import com.wsws.moduledomain.user.User;
import com.wsws.moduledomain.user.vo.SocialLoginInfo;

import java.util.Optional;

public interface SocialLoginRepository {
    Optional<SocialLoginInfo> findByProviderAndProviderId(String provider, String providerId);
    void save(String provider, String providerId, User user );
    boolean existsByProviderAndProviderId(String provider, String providerId);
}
