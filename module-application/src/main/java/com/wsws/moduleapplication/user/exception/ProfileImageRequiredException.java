package com.wsws.moduleapplication.user.exception;

import com.wsws.moduleapi.exception.ApplicationException;
import com.wsws.moduleapi.exception.DomainException;
import com.wsws.moduledomain.user.exception.UserErrorCode;

public class ProfileImageRequiredException extends ApplicationException {
    public static final ProfileImageRequiredException EXCEPTION = new ProfileImageRequiredException();

    private ProfileImageRequiredException() {
        super(UserServiceErrorCode.PROFILE_IMAGE_REQUIRED);
    }
}
