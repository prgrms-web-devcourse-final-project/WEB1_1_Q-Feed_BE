package com.wsws.moduleapplication.user.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class DuplicateNicknameException extends ApplicationException {
    public static final DuplicateNicknameException EXCEPTION = new DuplicateNicknameException();

    private DuplicateNicknameException() {
        super(UserServiceErrorCode.DUPLICATE_NICKNAME);
    }
}
