package com.wsws.moduleapplication.feed.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class AlreadyAnswerWrittenException extends ApplicationException {
    public static final AlreadyAnswerWrittenException EXCEPTION = new AlreadyAnswerWrittenException();

    private AlreadyAnswerWrittenException() {
        super(FeedServiceErrorCode.ALREADY_ANSWER_WRITTEN);
    }
}
