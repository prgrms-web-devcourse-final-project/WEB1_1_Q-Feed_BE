package com.wsws.moduledomain.authcontext.social;

import com.wsws.moduledomain.authcontext.social.aggregate.SocialLogin;

public interface SocialLoginService {
    SocialLogin getSocialLoginInfo(String authorizationCode);
}
