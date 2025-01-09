package com.wsws.moduleapplication.group.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class GroupCommentNotFoundException extends ApplicationException {
    public static final GroupCommentNotFoundException EXCEPTION = new GroupCommentNotFoundException();

    private GroupCommentNotFoundException() {
        super(GroupServiceErrorCode.GROUP_COMMENT_NOT_FOUND);
    }
}
