package com.wsws.moduleapplication.authcontext.exception;


import com.wsws.modulecommon.exception.ApplicationException;

public class InvalidRefreshTokenException extends ApplicationException {
    public static final InvalidRefreshTokenException EXCEPTION = new InvalidRefreshTokenException();

    private InvalidRefreshTokenException() {
        super(AuthServiceErrorCode.REFRESH_TOKEN_NOT_FOUND);
    }
}
