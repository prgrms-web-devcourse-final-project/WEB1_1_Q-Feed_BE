package com.wsws.moduleapplication.group.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class MemberNotFoundException extends ApplicationException {
    public static final MemberNotFoundException EXCEPTION = new MemberNotFoundException();

    private MemberNotFoundException() {
        super(GroupServiceErrorCode.MEMBER_NOT_FOUND);
    }
}
