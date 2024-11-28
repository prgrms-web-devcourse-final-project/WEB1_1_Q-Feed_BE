package com.wsws.modulesecurity.exception;

import com.wsws.moduleapi.exception.ApplicationException;
import com.wsws.moduleapi.exception.SecurityException;

public class InvalidTokenException extends SecurityException {
    public static final InvalidTokenException EXCEPTION = new InvalidTokenException();

    private InvalidTokenException() {
        super(SecurityErrorCode.INVALID_TOKEN);
    }
}