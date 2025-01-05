package com.wsws.moduledomain.usercontext.user.exception;

import com.wsws.modulecommon.exception.DomainException;

public class AlreadyInactiveUserException extends DomainException {
    public static final AlreadyInactiveUserException EXCEPTION = new AlreadyInactiveUserException();

    private AlreadyInactiveUserException() {
        super(UserErrorCode.ALREADY_INACTIVE_USER);
    }
}
