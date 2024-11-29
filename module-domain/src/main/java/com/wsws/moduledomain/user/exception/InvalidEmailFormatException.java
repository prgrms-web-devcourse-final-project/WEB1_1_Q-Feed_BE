package com.wsws.moduledomain.user.exception;

import com.wsws.moduleapi.exception.DomainException;

public class InvalidEmailFormatException extends DomainException {
    public static final InvalidEmailFormatException EXCEPTION = new InvalidEmailFormatException();

    private InvalidEmailFormatException() {
        super(UserErrorCode.INVALID_EMAIL_FORMAT);
    }

}
