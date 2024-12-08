package com.wsws.moduleapplication.feed.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class AnswerEditNotAllowedException extends ApplicationException {
    public static final AnswerEditNotAllowedException EXCEPTION = new AnswerEditNotAllowedException();

    private AnswerEditNotAllowedException() {
        super(FeedServiceErrorCode.ANSWER_EDIT_NOT_ALLOWED);
    }
}
