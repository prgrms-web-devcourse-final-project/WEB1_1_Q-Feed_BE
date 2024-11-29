package com.wsws.moduledomain.auth.exception;

import com.wsws.moduleapi.exception.DomainException;

public class InvalidRefreshTokenException extends DomainException {

    public static final InvalidRefreshTokenException EXCEPTION = new InvalidRefreshTokenException();
    public InvalidRefreshTokenException() {
        super(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND);
    }

}
