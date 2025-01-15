package com.wsws.moduleinfra.authcontext.auth.exception;

import com.wsws.modulecommon.exception.InfraException;

public class InvalidRefreshTokenDataException extends InfraException {

    public static final InvalidRefreshTokenDataException EXCEPTION = new InvalidRefreshTokenDataException();

    private InvalidRefreshTokenDataException() {
        super(AuthInfraErrorCode.INVALID_REFRESH_TOKEN_DATA);
    }
}
