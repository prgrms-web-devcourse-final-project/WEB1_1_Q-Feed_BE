package com.wsws.moduleapplication.user.exception;

import com.wsws.moduleapi.exception.ApplicationException;
import com.wsws.moduleapi.exception.DomainException;
import com.wsws.moduledomain.user.exception.UserErrorCode;

public class ProfileImageProcessingException extends ApplicationException {
    public static final ProfileImageProcessingException EXCEPTION = new ProfileImageProcessingException();

    private ProfileImageProcessingException() {
        super(UserServiceErrorCode.PROFILE_IMAGE_PROCESSING_ERROR);
    }
}
