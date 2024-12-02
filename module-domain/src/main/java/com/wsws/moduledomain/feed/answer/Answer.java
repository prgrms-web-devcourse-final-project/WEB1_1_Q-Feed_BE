package com.wsws.moduledomain.feed.answer;

import com.wsws.moduledomain.feed.answer.vo.AnswerId;
import com.wsws.moduledomain.feed.question.Question;
import com.wsws.moduledomain.feed.question.vo.QuestionId;
import com.wsws.moduledomain.user.vo.UserId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer {
    AnswerId answerId;
    String content;
    Boolean visibility;
    String url;
    int reactionCount;

    QuestionId questionId;
    UserId userId;

    public static Answer create(Long answerId, String content, Boolean visibility, String url, int reactionCount, Long questionId, String userId) {
        Answer answer = new Answer();
        answer.answerId = AnswerId.of(answerId);
        answer.content = content;
        answer.visibility = visibility;
        answer.url = url;
        answer.reactionCount = reactionCount;
        answer.questionId = QuestionId.of(questionId);
        answer.userId = UserId.of(userId);
        return answer;
    }
}
