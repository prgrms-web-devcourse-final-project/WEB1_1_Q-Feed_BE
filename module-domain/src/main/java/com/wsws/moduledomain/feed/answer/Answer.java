package com.wsws.moduledomain.feed.answer;

import com.wsws.moduledomain.feed.answer.vo.AnswerId;
import com.wsws.moduledomain.feed.comment.AnswerComment;
import com.wsws.moduledomain.feed.question.vo.QuestionId;
import com.wsws.moduledomain.user.vo.UserId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer {
    AnswerId answerId;
    String content;
    Boolean visibility;
    String url;
    int likeCount;
    LocalDateTime createdAt;

    QuestionId questionId;
    UserId userId;
    List<AnswerComment> comments = new ArrayList<>();

    public static Answer create(Long answerId, String content, Boolean visibility, String url, int likeCount, LocalDateTime createdAt, Long questionId, String userId) {
        Answer answer = new Answer();
        answer.answerId = AnswerId.of(answerId);
        answer.content = content;
        answer.visibility = visibility;
        answer.url = url;
        answer.likeCount = likeCount;
        answer.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        answer.questionId = QuestionId.of(questionId);
        answer.userId = UserId.of(userId);
        return answer;
    }

    /* 비즈니스 로직 */

    // 답변 수정
    public void editAnswer(String content, Boolean visibility, String url) {
        this.content = content;
        this.visibility = visibility;
        this.url = url;
    }

    // 공개 여부 수정
    public void changeVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    // 좋아요 추가
    public void addLikeCount() {
        this.likeCount++;
    }

    // 좋아요 취소
    public void cancelLikeCount() {
        this.likeCount--;
    }

}
