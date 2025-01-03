package com.wsws.moduleapplication.group.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class UnauthorizedAccessException extends ApplicationException {
    public static final UnauthorizedAccessException EXCEPTION = new UnauthorizedAccessException();

    private UnauthorizedAccessException() {
        super(GroupServiceErrorCode.UNAUTHORIZED_ACCESS);
    }
}
