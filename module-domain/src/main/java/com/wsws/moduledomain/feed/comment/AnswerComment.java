package com.wsws.moduledomain.feed.comment;

import com.wsws.moduledomain.feed.answer.vo.AnswerId;
import com.wsws.moduledomain.feed.comment.vo.AnswerCommentId;
import com.wsws.moduledomain.user.vo.UserId;
import lombok.Getter;

@Getter
public class AnswerComment {
    private AnswerCommentId answerCommentId;
    private String content;
    private int depth;
    private int reactionCount;

    private AnswerId answerId;
    private UserId userId;
    private AnswerCommentId parentAnswerCommentId;

    public static AnswerComment create(Long commentId, String content, int depth, int reactionCount, Long answerId, String userId, Long parentCommentId) {
        AnswerComment answerComment = new AnswerComment();
        answerComment.answerCommentId = AnswerCommentId.of(commentId);
        answerComment.content = content;
        answerComment.depth = depth;
        answerComment.reactionCount = reactionCount;
        answerComment.answerId = AnswerId.of(answerId);
        answerComment.userId = UserId.of(userId);
        answerComment.parentAnswerCommentId = AnswerCommentId.of(parentCommentId);
        return answerComment;
    }

    /* 비즈니스 로직 */
    public void editAnswerComment(String content) {
        this.content = content;
    }

    public void addReactionCount() {
        this.reactionCount++;
    }

    public void cancelReactionCount() {
        this.reactionCount--;
    }
}
