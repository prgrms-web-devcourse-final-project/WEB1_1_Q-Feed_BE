package com.wsws.moduleapplication.user.exception;

import com.wsws.moduleapi.exception.ApplicationException;
import com.wsws.moduleapi.exception.DomainException;
import com.wsws.moduledomain.user.exception.UserErrorCode;

public class DuplicateEmailException extends ApplicationException {
    public static final DuplicateEmailException EXCEPTION = new DuplicateEmailException();

    private DuplicateEmailException() {
        super(UserServiceErrorCode.DUPLICATE_EMAIL);
    }
}
