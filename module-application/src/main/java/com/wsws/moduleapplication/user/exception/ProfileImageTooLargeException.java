package com.wsws.moduleapplication.user.exception;

import com.wsws.moduleapi.exception.ApplicationException;
import com.wsws.moduleapi.exception.DomainException;
import com.wsws.moduledomain.user.exception.UserErrorCode;

public class ProfileImageTooLargeException extends ApplicationException {
    public static final ProfileImageTooLargeException EXCEPTION = new ProfileImageTooLargeException();

    private ProfileImageTooLargeException() {
        super(UserServiceErrorCode.PROFILE_IMAGE_TOO_LARGE);
    }
}
