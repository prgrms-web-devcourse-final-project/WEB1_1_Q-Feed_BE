package com.wsws.moduleapplication.follow.exception;

import com.wsws.moduleapi.exception.ApplicationException;

public class FollowNotFoundException extends ApplicationException {
    public static final FollowNotFoundException EXCEPTION = new FollowNotFoundException();

    private FollowNotFoundException() {
        super(FollowServiceErrorCode.FOLLOW_NOT_FOUND);
    }
}
