package com.wsws.moduleapplication.user.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class AlreadyLikedException extends ApplicationException {
    public static final AlreadyLikedException EXCEPTION = new AlreadyLikedException();

    private AlreadyLikedException() {
        super(UserServiceErrorCode.ALREADY_LIKED);
    }
}
