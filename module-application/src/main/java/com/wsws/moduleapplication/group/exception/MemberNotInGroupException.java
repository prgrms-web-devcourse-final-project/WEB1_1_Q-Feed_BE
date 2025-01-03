package com.wsws.moduleapplication.group.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class MemberNotInGroupException extends ApplicationException {
    public static final MemberNotInGroupException EXCEPTION = new MemberNotInGroupException();

    private MemberNotInGroupException() {
        super(GroupServiceErrorCode.MEMBER_NOT_IN_GROUP);
    }
}
