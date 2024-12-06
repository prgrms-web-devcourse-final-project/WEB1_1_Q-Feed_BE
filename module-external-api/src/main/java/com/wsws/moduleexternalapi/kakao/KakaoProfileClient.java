package com.wsws.moduleexternalapi.kakao;

import com.wsws.moduleexternalapi.kakao.dto.KakaoProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoProfileClient", url = "${kakao.api-url}")
public interface KakaoProfileClient {
    @GetMapping("/v2/user/me")
    KakaoProfileResponse getUserInfo(@RequestHeader("Authorization") String accessToken);
}
