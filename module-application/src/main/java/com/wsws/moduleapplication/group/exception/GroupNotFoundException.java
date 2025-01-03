package com.wsws.moduleapplication.group.exception;

import com.wsws.moduleapplication.group.exception.GroupServiceErrorCode;
import com.wsws.modulecommon.exception.ApplicationException;

public class GroupNotFoundException extends ApplicationException {
    public static final GroupNotFoundException EXCEPTION = new GroupNotFoundException();

    private GroupNotFoundException() {
        super(GroupServiceErrorCode.GROUP_NOT_FOUND);
    }
}
