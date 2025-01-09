package com.wsws.moduleapplication.group.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class NotOwnerException extends ApplicationException {
    public static final NotOwnerException EXCEPTION = new NotOwnerException();

    private NotOwnerException() {
        super(GroupServiceErrorCode.NOT_OWNER);
    }
}
