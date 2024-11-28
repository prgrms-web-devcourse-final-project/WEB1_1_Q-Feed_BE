package com.wsws.moduleapplication.auth.exception;

import com.wsws.moduleapi.exception.ApplicationException;

public class InvalidRefreshTokenException extends ApplicationException {
    public static final InvalidRefreshTokenException EXCEPTION = new InvalidRefreshTokenException();

    private InvalidRefreshTokenException() {
        super(AuthServiceErrorCode.REFRESH_TOKEN_NOT_FOUND);
    }
}
