package com.wsws.moduleapplication.user.exception;


import com.wsws.modulecommon.exception.ApplicationException;

public class ProfileImageRequiredException extends ApplicationException {
    public static final ProfileImageRequiredException EXCEPTION = new ProfileImageRequiredException();

    private ProfileImageRequiredException() {
        super(UserServiceErrorCode.PROFILE_IMAGE_REQUIRED);
    }
}
