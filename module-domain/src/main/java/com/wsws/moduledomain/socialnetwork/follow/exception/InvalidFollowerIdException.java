package com.wsws.moduledomain.socialnetwork.follow.exception;

import com.wsws.modulecommon.exception.DomainException;

public class InvalidFollowerIdException extends DomainException {

    public static final InvalidFollowerIdException EXCEPTION = new InvalidFollowerIdException();

    private InvalidFollowerIdException() {
        super(FollowErrorCode.EMPTY_FOLLOWER_ID);
    }
}
