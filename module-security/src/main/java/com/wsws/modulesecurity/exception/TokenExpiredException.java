package com.wsws.modulesecurity.exception;

import com.wsws.modulecommon.exception.SecurityException;

public class TokenExpiredException extends SecurityException {
    public static final TokenExpiredException EXCEPTION = new TokenExpiredException();

    private TokenExpiredException() {
        super(SecurityErrorCode.TOKEN_EXPIRED);
    }
}
