package com.wsws.moduleapplication.feed.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class QuestionNotFoundException extends ApplicationException {
    public static final QuestionNotFoundException EXCEPTION = new QuestionNotFoundException();

    private QuestionNotFoundException() {
        super(FeedServiceErrorCode.QUESTION_NOT_FOUND);
    }
}
