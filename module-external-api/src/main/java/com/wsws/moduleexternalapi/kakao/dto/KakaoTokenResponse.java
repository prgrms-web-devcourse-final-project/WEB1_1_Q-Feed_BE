package com.wsws.moduleexternalapi.kakao.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoTokenResponse {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private int expires_in;
    private int refresh_token_expires_in;
}
