package com.wsws.moduleapplication.group.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class GroupPostNotFoundException extends ApplicationException {
    public static final GroupPostNotFoundException EXCEPTION = new GroupPostNotFoundException();

    private GroupPostNotFoundException() {
        super(GroupServiceErrorCode.GROUP_POST_NOT_FOUND);
    }
}
