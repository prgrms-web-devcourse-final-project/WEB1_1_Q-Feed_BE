package com.wsws.moduledomain.authcontext.social;

import com.wsws.moduledomain.authcontext.social.aggregate.SocialLogin;

import java.util.Optional;

public interface SocialLoginRepository {
    void save(String provider, String providerId, String userId );
    boolean existsByProviderAndProviderId(String provider, String providerId);
}
