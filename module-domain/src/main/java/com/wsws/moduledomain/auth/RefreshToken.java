package com.wsws.moduledomain.auth;

import com.wsws.moduledomain.auth.exception.ExpiredRefreshTokenException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RefreshToken {

    private final String token;
    private final LocalDateTime expiryDate;

    private RefreshToken(String token, LocalDateTime expiryDate) {
        this.token = token;
        this.expiryDate = expiryDate;
    }

    public static RefreshToken create(String token, LocalDateTime expiryDate) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty.");
        }
        if (expiryDate == null || expiryDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid expiry date.");
        }
        return new RefreshToken(token, expiryDate);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }

    public void validateExpiry() {
        if (isExpired()) {
            throw new ExpiredRefreshTokenException("Refresh token has expired.");
        }
    }
}
