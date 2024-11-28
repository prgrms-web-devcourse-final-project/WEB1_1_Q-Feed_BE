package com.wsws.moduledomain.auth.exception;

import com.wsws.moduleapi.exception.BaseErrorCode;
import com.wsws.moduleapi.exception.DomainException;

public class ExpiredRefreshTokenException extends DomainException {

    public static final ExpiredRefreshTokenException EXCEPTION = new ExpiredRefreshTokenException();

    public ExpiredRefreshTokenException() {
        super(AuthErrorCode.REFRESH_TOKEN_IS_EXPIRED);
    }

}
