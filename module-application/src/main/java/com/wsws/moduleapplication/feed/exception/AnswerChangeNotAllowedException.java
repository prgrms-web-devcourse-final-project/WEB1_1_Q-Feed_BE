package com.wsws.moduleapplication.feed.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class AnswerChangeNotAllowedException extends ApplicationException {
    public static final AnswerChangeNotAllowedException EXCEPTION = new AnswerChangeNotAllowedException();

    private AnswerChangeNotAllowedException() {
        super(FeedServiceErrorCode.ANSWER_CHANGE_NOT_ALLOWED);
    }
}
