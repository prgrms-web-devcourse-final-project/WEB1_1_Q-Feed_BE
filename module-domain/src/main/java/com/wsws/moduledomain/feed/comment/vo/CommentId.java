package com.wsws.moduledomain.feed.comment.vo;

import com.wsws.moduledomain.feed.answer.vo.AnswerId;
import com.wsws.moduledomain.user.vo.LikeTargetId;
import lombok.Getter;

@Getter
public class CommentId extends LikeTargetId {

    private CommentId(Long value) {
        super(value);
    }

    public static CommentId of(Long id) {
        return new CommentId(id);
    }
}
