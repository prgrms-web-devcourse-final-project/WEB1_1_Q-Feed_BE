package com.wsws.moduledomain.usercontext.user.exception;


import com.wsws.modulecommon.exception.DomainException;

public class InvalidPasswordFormatException extends DomainException {

    public static final InvalidPasswordFormatException EXCEPTION = new InvalidPasswordFormatException();

    private InvalidPasswordFormatException() {
        super(UserErrorCode.INVALID_PASSWORD_FORMAT);
    }
}
