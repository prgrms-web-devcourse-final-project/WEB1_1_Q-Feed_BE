package com.wsws.moduleapplication.follow.exception;


import com.wsws.modulecommon.exception.ApplicationException;

public class AlreadyFollowedException extends ApplicationException {
    public static final AlreadyFollowedException EXCEPTION = new AlreadyFollowedException();

    private AlreadyFollowedException() {
        super(FollowServiceErrorCode.ALREADY_FOLLOWED);
    }
}
