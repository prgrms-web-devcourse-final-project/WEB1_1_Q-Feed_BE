package com.wsws.moduledomain.authcontext.exception;


import com.wsws.modulecommon.exception.DomainException;

public class ExpiredRefreshTokenException extends DomainException {

    public static final ExpiredRefreshTokenException EXCEPTION = new ExpiredRefreshTokenException();

    public ExpiredRefreshTokenException() {
        super(AuthErrorCode.REFRESH_TOKEN_IS_EXPIRED);
    }

}
