package com.wsws.moduleapplication.feed.exception;

import com.wsws.moduleapplication.user.exception.UserNotFoundException;
import com.wsws.moduleapplication.user.exception.UserServiceErrorCode;
import com.wsws.modulecommon.exception.ApplicationException;
import com.wsws.modulecommon.exception.BaseErrorCode;

public class QuestionNotFoundException extends ApplicationException {
    public static final QuestionNotFoundException EXCEPTION = new QuestionNotFoundException();

    private QuestionNotFoundException() {
        super(FeedServiceErrorCode.QUESTION_NOT_FOUND);
    }
}
