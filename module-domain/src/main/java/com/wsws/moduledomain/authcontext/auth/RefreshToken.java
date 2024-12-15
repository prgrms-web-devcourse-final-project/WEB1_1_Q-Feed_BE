package com.wsws.moduledomain.authcontext.auth;

import com.wsws.moduledomain.authcontext.exception.ExpiredRefreshTokenException;
import com.wsws.moduledomain.authcontext.exception.InvalidRefreshTokenException;
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
            throw  InvalidRefreshTokenException.EXCEPTION;
        }
        if (expiryDate == null || expiryDate.isBefore(LocalDateTime.now())) {
            throw ExpiredRefreshTokenException.EXCEPTION;
        }
        return new RefreshToken(token, expiryDate);
    }

    private boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }

    public void validateExpiry() {
        if (isExpired()) {
            throw ExpiredRefreshTokenException.EXCEPTION;
        }
    }
}
