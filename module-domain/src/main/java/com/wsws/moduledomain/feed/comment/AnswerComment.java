package com.wsws.moduledomain.feed.comment;

import com.wsws.moduledomain.feed.answer.vo.AnswerId;
import com.wsws.moduledomain.feed.comment.vo.AnswerCommentId;
import com.wsws.moduledomain.user.Like;
import com.wsws.moduledomain.user.vo.UserId;
import lombok.Getter;
import org.checkerframework.checker.units.qual.A;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class AnswerComment {
    private AnswerCommentId answerCommentId;
    private String content;
    private int depth;
    private int likeCount;
    private LocalDateTime createdAt;

    private AnswerId answerId;
    private UserId userId;
    private AnswerCommentId parentAnswerCommentId;
    private List<AnswerComment> childrenComment = new ArrayList<>();

    public static AnswerComment create(Long commentId, String content, int depth, int likeCount, LocalDateTime createdAt, Long answerId, String userId, Long parentCommentId) {
        AnswerComment answerComment = new AnswerComment();
        answerComment.answerCommentId = AnswerCommentId.of(commentId);
        answerComment.content = content;
        answerComment.depth = depth;
        answerComment.likeCount = likeCount;
        answerComment.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        answerComment.answerId = AnswerId.of(answerId);
        answerComment.userId = UserId.of(userId);
        answerComment.parentAnswerCommentId = AnswerCommentId.of(parentCommentId);
        answerComment.createdAt = LocalDateTime.now();
        return answerComment;
    }

    /* 비즈니스 로직 */
    public void editAnswerComment(String content) {
        this.content = content;
    }

    public void addLikeCount() {
        this.likeCount++;
    }

    public void cancelLikeCount() {
        this.likeCount--;
    }
}
