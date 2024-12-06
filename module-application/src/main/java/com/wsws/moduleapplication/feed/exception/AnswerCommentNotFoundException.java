package com.wsws.moduleapplication.feed.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class AnswerCommentNotFoundException extends ApplicationException {
    public static final AnswerCommentNotFoundException EXCEPTION = new AnswerCommentNotFoundException();

    private AnswerCommentNotFoundException() {
        super(FeedServiceErrorCode.ANSWER_COMMENT_NOT_FOUND);
    }
}
