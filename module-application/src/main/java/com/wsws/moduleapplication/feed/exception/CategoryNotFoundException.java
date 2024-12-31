package com.wsws.moduleapplication.feed.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class CategoryNotFoundException extends ApplicationException {

    public CategoryNotFoundException() {
        super(FeedServiceErrorCode.CATEGORY_NOT_FOUND);
    }

    public CategoryNotFoundException(String message) {
        super(FeedServiceErrorCode.CATEGORY_NOT_FOUND, message);
    }
}
