package com.wsws.moduleapplication.auth.dto;

public record TokenReissueAppDto(String accessToken, String refreshToken) {
}
