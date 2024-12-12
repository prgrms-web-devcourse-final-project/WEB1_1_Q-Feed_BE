package com.wsws.moduledomain.authcontext.exception;


import com.wsws.modulecommon.exception.DomainException;

public class PasswordMismatchException extends DomainException {
    public static final PasswordMismatchException EXCEPTION = new PasswordMismatchException();

    private PasswordMismatchException() {
        super(AuthErrorCode.PASSWORD_MISMATCH); // AuthErrorCode와 연결
    }
}
