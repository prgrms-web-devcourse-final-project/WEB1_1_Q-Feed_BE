package com.wsws.moduleapplication.user.exception;

import com.wsws.moduleapi.exception.ApplicationException;
import com.wsws.moduleapi.exception.DomainException;
import com.wsws.moduledomain.user.exception.UserErrorCode;

public class UserNotFoundException extends ApplicationException {

    public static final UserNotFoundException EXCEPTION = new UserNotFoundException();

    private UserNotFoundException() {
        super(UserServiceErrorCode.USER_NOT_FOUND);
    }
}
