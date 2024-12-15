package com.wsws.moduledomain.authcontext.exception;


import com.wsws.modulecommon.exception.DomainException;

public class InvalidRefreshTokenException extends DomainException {

    public static final InvalidRefreshTokenException EXCEPTION = new InvalidRefreshTokenException();
    public InvalidRefreshTokenException() {
        super(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND);
    }

}
