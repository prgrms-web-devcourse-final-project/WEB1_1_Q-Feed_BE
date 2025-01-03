package com.wsws.moduleapplication.chat.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class UnauthorizedAccessException extends ApplicationException {
    public static final UnauthorizedAccessException EXCEPTION = new UnauthorizedAccessException();

    private UnauthorizedAccessException() {
        super(ChatServiceErrorCode.UNAUTHORIZED_ACCESS);
    }
}
