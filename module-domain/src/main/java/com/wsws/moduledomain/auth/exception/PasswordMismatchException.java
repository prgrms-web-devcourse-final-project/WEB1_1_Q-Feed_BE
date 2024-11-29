package com.wsws.moduledomain.auth.exception;

import com.wsws.moduleapi.exception.ApplicationException;
import com.wsws.moduleapi.exception.DomainException;
import com.wsws.moduledomain.user.exception.UserErrorCode;


public class PasswordMismatchException extends DomainException {
    public static final PasswordMismatchException EXCEPTION = new PasswordMismatchException();

    private PasswordMismatchException() {
        super(AuthErrorCode.PASSWORD_MISMATCH); // AuthErrorCode와 연결
    }
}
