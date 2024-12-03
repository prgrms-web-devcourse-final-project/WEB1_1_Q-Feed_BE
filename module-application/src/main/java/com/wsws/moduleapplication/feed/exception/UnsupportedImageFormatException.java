package com.wsws.moduleapplication.feed.exception;

import com.wsws.moduleapplication.user.exception.UserServiceErrorCode;
import com.wsws.modulecommon.exception.ApplicationException;

public class UnsupportedImageFormatException extends ApplicationException {
    public static final UnsupportedImageFormatException EXCEPTION = new UnsupportedImageFormatException();

    private UnsupportedImageFormatException() {
        super(FeedServiceErrorCode.UNSUPPORTED_FILE_FORMAT);
    }
}
