package com.wsws.moduleapplication.auth.exception;


import com.wsws.moduleapi.exception.ApplicationException;

public class RefreshTokenExpiredException extends ApplicationException {
    public static final RefreshTokenExpiredException EXCEPTION = new RefreshTokenExpiredException();

    private RefreshTokenExpiredException() {
        super(AuthServiceErrorCode.REFRESH_TOKEN_EXPIRED);
    }
}
