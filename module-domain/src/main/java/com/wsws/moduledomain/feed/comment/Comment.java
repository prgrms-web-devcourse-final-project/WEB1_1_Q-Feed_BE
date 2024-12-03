package com.wsws.moduledomain.feed.comment;

import com.wsws.moduledomain.feed.answer.vo.AnswerId;
import com.wsws.moduledomain.feed.comment.vo.CommentId;
import com.wsws.moduledomain.user.vo.UserId;
import lombok.Getter;

@Getter
public class Comment {
    CommentId commentId;
    String content;
    int depth;
    int reactionCount;

    AnswerId answerId;
    UserId userId;
    CommentId parentCommentId;

    public static Comment create(Long commentId, String content, int depth, int reactionCount, Long answerId, String userId, Long parentCommentId) {
        Comment comment = new Comment();
        comment.commentId = CommentId.of(commentId);
        comment.content = content;
        comment.depth = depth;
        comment.reactionCount = reactionCount;
        comment.answerId = AnswerId.of(answerId);
        comment.userId = UserId.of(userId);
        comment.parentCommentId = CommentId.of(parentCommentId);
        return comment;
    }
}
