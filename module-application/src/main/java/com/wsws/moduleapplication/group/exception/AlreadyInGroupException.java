package com.wsws.moduleapplication.group.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class AlreadyInGroupException extends ApplicationException {
    public static final AlreadyInGroupException EXCEPTION = new AlreadyInGroupException();

    private AlreadyInGroupException() {
        super(GroupServiceErrorCode.ALREADY_IN_GROUP);
    }
}
