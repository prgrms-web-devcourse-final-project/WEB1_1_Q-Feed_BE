package com.wsws.moduleapplication.feed.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class ParentAnswerCommentNotFoundException extends ApplicationException {
    public static final ParentAnswerCommentNotFoundException EXCEPTION = new ParentAnswerCommentNotFoundException();

    private ParentAnswerCommentNotFoundException() {
        super(FeedServiceErrorCode.PARENT_ANSWER_COMMENT_NOT_FOUND);
    }
}
