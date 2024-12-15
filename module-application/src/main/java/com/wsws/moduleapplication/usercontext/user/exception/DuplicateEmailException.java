package com.wsws.moduleapplication.usercontext.user.exception;


import com.wsws.modulecommon.exception.ApplicationException;

public class DuplicateEmailException extends ApplicationException {
    public static final DuplicateEmailException EXCEPTION = new DuplicateEmailException();

    private DuplicateEmailException() {
        super(UserServiceErrorCode.DUPLICATE_EMAIL);
    }
}
