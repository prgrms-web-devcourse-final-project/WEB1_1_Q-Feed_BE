package com.wsws.moduleapplication.feed.service;

import com.wsws.moduleapplication.feed.dto.answer_comment.AnswerCommentCreateServiceRequest;
import com.wsws.moduleapplication.feed.dto.answer_comment.AnswerCommentCreateServiceResponse;
import com.wsws.moduleapplication.feed.dto.answer_comment.AnswerCommentEditServiceRequest;
import com.wsws.moduleapplication.feed.exception.AnswerCommentNotFoundException;
import com.wsws.moduleapplication.feed.exception.ParentAnswerCommentNotFoundException;
import com.wsws.moduleapplication.user.dto.LikeServiceRequest;
import com.wsws.moduleapplication.user.exception.AlreadyLikedException;
import com.wsws.moduleapplication.user.exception.NotLikedException;
import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduledomain.feed.answer.repo.AnswerRepository;
import com.wsws.moduledomain.feed.comment.AnswerComment;
import com.wsws.moduledomain.feed.comment.repo.AnswerCommentRepository;
import com.wsws.moduledomain.user.Like;
import com.wsws.moduledomain.user.repo.LikeRepository;
import com.wsws.moduledomain.user.repo.UserRepository;
import com.wsws.moduledomain.user.vo.TargetType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerCommentService {

    private final AnswerCommentRepository answerCommentRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    /**
     * 답변 댓글 추가
     */
    public AnswerCommentCreateServiceResponse createAnswerComment(AnswerCommentCreateServiceRequest request) {
        int depth = 0;
        Long parentCommentId = request.parentCommentId();
        AnswerComment parentAnswerComment = null;
        if(parentCommentId != null) {
            parentAnswerComment = answerCommentRepository.findById(parentCommentId)
                    .orElseThrow(() -> ParentAnswerCommentNotFoundException.EXCEPTION); // 부모 댓글 불러오기
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

    /**
     * 답변 댓글 좋아요 추가
     * TODO: 추후 레디스로 분산락 적용해 동시성 해결
     * TODO: 중복코드 발생. 추후 패서드 패턴 적용
     */
    public void addLikeToAnswerComment(LikeServiceRequest request) {

        createLike(request); // like 객체 생성

        AnswerComment answerComment = answerCommentRepository.findById(request.targetId())
                .orElseThrow(() -> AnswerCommentNotFoundException.EXCEPTION);

        answerComment.addLikeCount(); // 좋아요 1 추가

        answerCommentRepository.edit(answerComment); // 수정사항 반영
    }


    /**
     * 답변 댓글 좋아요 취소
     */
    public void cancelLikeToCommentAnswer(LikeServiceRequest request) {
        AnswerComment answerComment = answerCommentRepository.findById(request.targetId())
                .orElseThrow(() -> AnswerCommentNotFoundException.EXCEPTION);

        deleteLike(request); // 기존 좋아요 객체 삭제

        answerComment.cancelLikeCount(); // AnswerComment의 likeCount 1감소

        answerCommentRepository.edit(answerComment); // 수정 반영

    }



    /* private 메서드 */

    private Answer getRelatedAnswer(Long answerId) {
        return answerRepository.findById(answerId)
                .orElseThrow(() -> AnswerCommentNotFoundException.EXCEPTION);
    }

    /**
     * Like 저장 생성 및 저장
     */
    private void createLike(LikeServiceRequest request) {

        if (isAlreadyLike(request.targetId(), request.userId(), TargetType.valueOf(request.targetType()))) // 좋아요를 누른적이 있다면 예외
            throw AlreadyLikedException.EXCEPTION;


        Like like = Like.create(
                null,
                TargetType.valueOf(request.targetType()),
                request.targetId(),
                request.userId()
        );
        likeRepository.save(like);
    }

    /**
     * Like 삭제
     */
    private void deleteLike(LikeServiceRequest request) {
        if (!isAlreadyLike(request.targetId(), request.userId(), TargetType.valueOf(request.targetType()))) // 좋아요를 누른적이 없다면 예외
            throw NotLikedException.EXCEPTION;

        likeRepository.deleteByTargetIdAndUserId(request.targetId(), request.userId()); // 해당 좋아요 정보 삭제
    }


    /**
     * 같은 글에 좋아요를 누른적이 있는지 확인
     */
    private boolean isAlreadyLike(Long targetId, String userId, TargetType targetType) {
        return likeRepository.existsByTargetIdAndUserIdAndTargetType(targetId, userId, targetType);
    }
}
