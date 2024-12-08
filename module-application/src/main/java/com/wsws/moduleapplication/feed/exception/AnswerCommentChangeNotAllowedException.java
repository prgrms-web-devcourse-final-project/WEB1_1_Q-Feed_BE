package com.wsws.moduleapplication.feed.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class AnswerCommentChangeNotAllowedException extends ApplicationException {
    public static final AnswerCommentChangeNotAllowedException EXCEPTION = new AnswerCommentChangeNotAllowedException();

    private AnswerCommentChangeNotAllowedException() {
        super(FeedServiceErrorCode.ANSWER_COMMENT_CHANGE_NOT_ALLOWED);
    }
}
