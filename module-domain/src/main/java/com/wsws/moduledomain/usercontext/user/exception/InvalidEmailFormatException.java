package com.wsws.moduledomain.usercontext.user.exception;


import com.wsws.modulecommon.exception.DomainException;

public class InvalidEmailFormatException extends DomainException {
    public static final InvalidEmailFormatException EXCEPTION = new InvalidEmailFormatException();

    private InvalidEmailFormatException() {
        super(UserErrorCode.INVALID_EMAIL_FORMAT);
    }

}
