package com.wsws.modulesecurity.exception;


import com.wsws.moduleapi.exception.SecurityException;

public class UserNotFoundException extends SecurityException {
    public static final UserNotFoundException EXCEPTION = new UserNotFoundException();

    private UserNotFoundException() {
        super(SecurityErrorCode.USER_NOT_FOUND);
    }
}
