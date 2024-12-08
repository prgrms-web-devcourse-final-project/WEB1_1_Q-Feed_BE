package com.wsws.moduleapi.auth.dto;

import com.wsws.moduleapplication.auth.dto.TokenReissueAppDto;

public record TokenReissueResponse(String accessToken, String refreshToke) {
    public TokenReissueResponse(TokenReissueAppDto dto){
        this(dto.accessToken(), dto.refreshToken());
    }
}
