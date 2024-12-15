package com.wsws.moduledomain.feed.comment.vo;

import com.wsws.moduledomain.feed.like.LikeTargetId;
import lombok.Getter;

@Getter
public class AnswerCommentId extends LikeTargetId {

    private AnswerCommentId(Long value) {
        super(value);
    }

    public static AnswerCommentId of(Long id) {
        return new AnswerCommentId(id);
    }
}
