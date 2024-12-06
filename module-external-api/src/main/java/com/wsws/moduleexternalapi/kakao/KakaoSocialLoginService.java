package com.wsws.moduleexternalapi.kakao;

import com.wsws.moduledomain.user.repo.SocialLoginService;
import com.wsws.moduledomain.user.vo.SocialLoginInfo;
import com.wsws.moduleexternalapi.kakao.dto.KakaoProfileResponse;
import com.wsws.moduleexternalapi.kakao.dto.KakaoTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoSocialLoginService implements SocialLoginService {

    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoProfileClient kakaoProfileClient;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Override
    public SocialLoginInfo getSocialLoginInfo(String authorizationCode) {
        KakaoTokenResponse tokenResponse = kakaoAuthClient.getToken(
                kakaoClientId,
                kakaoRedirectUri,
                authorizationCode,
                "authorization_code"
        );

        KakaoProfileResponse profile = kakaoProfileClient.getUserInfo("Bearer " + tokenResponse.getAccess_token());

        return new SocialLoginInfo(
                "kakao",
                profile.getId(),
                profile.getKakao_account().getEmail(),
                profile.getKakao_account().getProfile().getNickname(),
                profile.getKakao_account().getProfile().getProfile_image_url()
        );
    }
}