package com.wsws.moduleapplication.user.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class AlreadyLikeException extends ApplicationException {
    public static final AlreadyLikeException EXCEPTION = new AlreadyLikeException();

    private AlreadyLikeException() {
        super(UserServiceErrorCode.ALREADY_LIKE);
    }
}
