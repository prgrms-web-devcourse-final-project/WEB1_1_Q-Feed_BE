package com.wsws.moduledomain.socialnetwork.follow.exception;

import com.wsws.modulecommon.exception.DomainException;

public class InvalidFollowException extends DomainException {

    public static final InvalidFollowException EXCEPTION = new InvalidFollowException();

    private InvalidFollowException() { super(FollowErrorCode.SELF_FOLLOW_NOT_ALLOWED);}
}
