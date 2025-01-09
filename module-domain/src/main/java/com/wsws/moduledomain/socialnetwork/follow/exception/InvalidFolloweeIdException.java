package com.wsws.moduledomain.socialnetwork.follow.exception;

import com.wsws.modulecommon.exception.DomainException;

public class InvalidFolloweeIdException extends DomainException {

    public static final InvalidFolloweeIdException EXCEPTION = new InvalidFolloweeIdException();

    private InvalidFolloweeIdException() {
        super(FollowErrorCode.EMPTY_FOLLOWEE_ID);
    }
}
