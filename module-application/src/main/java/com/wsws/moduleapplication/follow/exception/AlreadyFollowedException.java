package com.wsws.moduleapplication.follow.exception;


import com.wsws.moduleapi.exception.ApplicationException;

public class AlreadyFollowedException extends ApplicationException {
    public static final AlreadyFollowedException EXCEPTION = new AlreadyFollowedException();

    private AlreadyFollowedException() {
        super(FollowServiceErrorCode.ALREADY_FOLLOWED);
    }
}
