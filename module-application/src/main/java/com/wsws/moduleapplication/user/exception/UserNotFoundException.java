package com.wsws.moduleapplication.user.exception;


import com.wsws.modulecommon.exception.ApplicationException;

public class UserNotFoundException extends ApplicationException {

    public static final UserNotFoundException EXCEPTION = new UserNotFoundException();

    private UserNotFoundException() {
        super(UserServiceErrorCode.USER_NOT_FOUND);
    }
}
