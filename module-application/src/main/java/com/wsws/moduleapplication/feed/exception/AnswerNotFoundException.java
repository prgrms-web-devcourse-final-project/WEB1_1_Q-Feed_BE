package com.wsws.moduleapplication.feed.exception;

import com.wsws.modulecommon.exception.ApplicationException;
import com.wsws.modulecommon.exception.BaseErrorCode;

public class AnswerNotFoundException extends ApplicationException {
    public static final AnswerNotFoundException EXCEPTION = new AnswerNotFoundException();

    private AnswerNotFoundException() {
        super(FeedServiceErrorCode.ANSWER_NOT_FOUND);
    }
}
