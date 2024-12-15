package com.wsws.moduleinfra.usercontext.exception;

import com.wsws.modulecommon.exception.InfraException;

public class UserNotFoundException extends InfraException {
    public static final UserNotFoundException EXCEPTION = new UserNotFoundException();

    private UserNotFoundException() {
        super(UserInfraErrorCode.USER_NOT_FOUND);
    }
}
