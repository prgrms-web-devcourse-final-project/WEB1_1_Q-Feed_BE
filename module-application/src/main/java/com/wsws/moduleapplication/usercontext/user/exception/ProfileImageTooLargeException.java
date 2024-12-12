package com.wsws.moduleapplication.usercontext.user.exception;


import com.wsws.modulecommon.exception.ApplicationException;

public class ProfileImageTooLargeException extends ApplicationException {
    public static final ProfileImageTooLargeException EXCEPTION = new ProfileImageTooLargeException();

    private ProfileImageTooLargeException() {
        super(UserServiceErrorCode.PROFILE_IMAGE_TOO_LARGE);
    }
}
