package com.wsws.moduledomain.user.repo;

import com.wsws.moduledomain.user.vo.SocialLoginInfo;

public interface SocialLoginService {
    SocialLoginInfo getSocialLoginInfo(String authorizationCode);
}
