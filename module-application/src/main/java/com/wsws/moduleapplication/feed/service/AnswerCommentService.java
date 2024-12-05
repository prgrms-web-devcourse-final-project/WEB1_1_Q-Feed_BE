package com.wsws.moduleapplication.feed.service;

import com.wsws.moduleapplication.feed.dto.answer_comment.AnswerCommentCreateServiceRequest;
import com.wsws.moduleapplication.feed.dto.answer_comment.AnswerCommentCreateServiceResponse;
import com.wsws.moduleapplication.feed.dto.answer_comment.AnswerCommentEditServiceRequest;
import com.wsws.moduleapplication.feed.exception.AnswerCommentNotFoundException;
import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduledomain.feed.answer.repo.AnswerRepository;
import com.wsws.moduledomain.feed.comment.AnswerComment;
import com.wsws.moduledomain.feed.comment.repo.AnswerCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerCommentService {

    private final AnswerCommentRepository answerCommentRepository;
    private final AnswerRepository answerRepository;

    /**
     * 답변 댓글 추가
     */
    public AnswerCommentCreateServiceResponse createAnswerComment(AnswerCommentCreateServiceRequest request) {
        int depth = 0;
        Long parentCommentId = request.parentCommentId();
        AnswerComment parentAnswerComment = null;
        if(parentCommentId != null) {
            parentAnswerComment = answerCommentRepository.findById(parentCommentId)
                    .orElseThrow(() -> AnswerCommentNotFoundException.EXCEPTION); // 부모 댓글 불러오기
            depth = parentAnswerComment.getDepth() + 1;// 부모 댓글의 depth + 1
        }
        AnswerComment answerComment = AnswerComment.create(
                null,
                request.content(),
                depth,
                0,
                request.answerId(),
                request.userId(),
                parentCommentId
        );

        AnswerComment saved = answerCommentRepository.save(answerComment);
        return new AnswerCommentCreateServiceResponse(saved.getAnswerCommentId().getValue());
    }

    /**
     * 답변 댓글 수정
     */
    public void editAnswerComment(AnswerCommentEditServiceRequest request) {
        AnswerComment answerComment = answerCommentRepository.findById(request.answerCommentId())
                .orElseThrow(() -> AnswerCommentNotFoundException.EXCEPTION);

        answerComment.editAnswerComment(request.content());

        answerCommentRepository.edit(answerComment);
    }


    /**
     * 답변 댓글 삭제
     */
    public void deleteAnswerComment(Long answerCommentId) {
        answerCommentRepository.deleteById(answerCommentId);
    }


    private Answer getRelatedAnswer(Long answerId) {
        return answerRepository.findById(answerId)
                .orElseThrow(() -> AnswerCommentNotFoundException.EXCEPTION);
    }

}
