package com.wsws.moduleapplication.user.exception;

import com.wsws.moduleapi.exception.ApplicationException;
import com.wsws.moduleapi.exception.DomainException;
import com.wsws.moduledomain.user.exception.UserErrorCode;

public class DuplicateNicknameException extends ApplicationException {
    public static final DuplicateNicknameException EXCEPTION = new DuplicateNicknameException();

    private DuplicateNicknameException() {
        super(UserServiceErrorCode.DUPLICATE_NICKNAME);
    }
}
