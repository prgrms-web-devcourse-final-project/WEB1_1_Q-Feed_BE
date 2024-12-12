package com.wsws.moduleapplication.usercontext.user.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class NotLikedException extends ApplicationException {
    public static final NotLikedException EXCEPTION = new NotLikedException();

    private NotLikedException() {
        super(UserServiceErrorCode.NOT_LIKED);
    }
}
