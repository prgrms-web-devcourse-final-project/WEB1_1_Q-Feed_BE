package com.wsws.moduleapplication.feed.exception;


import com.wsws.modulecommon.exception.ApplicationException;

public class FeedImageProcessingException extends ApplicationException {
    public static final FeedImageProcessingException EXCEPTION = new FeedImageProcessingException();

    private FeedImageProcessingException() {
        super(FeedServiceErrorCode.FILE_PROCESSING_ERROR);
    }
}
