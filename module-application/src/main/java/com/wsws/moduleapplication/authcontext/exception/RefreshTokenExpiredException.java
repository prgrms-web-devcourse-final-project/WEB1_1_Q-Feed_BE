package com.wsws.moduleapplication.authcontext.exception;


import com.wsws.modulecommon.exception.ApplicationException;

public class RefreshTokenExpiredException extends ApplicationException {
    public static final RefreshTokenExpiredException EXCEPTION = new RefreshTokenExpiredException();

    private RefreshTokenExpiredException() {
        super(AuthServiceErrorCode.REFRESH_TOKEN_EXPIRED);
    }
}
