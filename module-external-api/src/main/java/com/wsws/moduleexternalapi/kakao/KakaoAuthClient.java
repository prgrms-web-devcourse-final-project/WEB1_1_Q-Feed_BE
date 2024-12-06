package com.wsws.moduleexternalapi.kakao;

import com.wsws.moduleexternalapi.kakao.dto.KakaoTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakaoAuthClient", url = "${kakao.auth-url}")
public interface KakaoAuthClient {
    @PostMapping(value = "/oauth/token", consumes = "application/x-www-form-urlencoded")
    KakaoTokenResponse getToken(
            @RequestParam("client_id") String clientId,
            @RequestParam("redirect_uri") String redirectUri,
            @RequestParam("code") String code,
            @RequestParam("grant_type") String grantType
    );
}
