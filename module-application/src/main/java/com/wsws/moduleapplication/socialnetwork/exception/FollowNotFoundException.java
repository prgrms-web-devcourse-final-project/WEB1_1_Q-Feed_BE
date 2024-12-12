package com.wsws.moduleapplication.socialnetwork.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class FollowNotFoundException extends ApplicationException {
    public static final FollowNotFoundException EXCEPTION = new FollowNotFoundException();

    private FollowNotFoundException() {
        super(FollowServiceErrorCode.FOLLOW_NOT_FOUND);
    }
}
