package com.wsws.moduleapplication.feed.service;

import com.wsws.moduleapplication.feed.dto.answer.edit.AnswerCreateServiceRequest;
import com.wsws.moduleapplication.feed.dto.answer.edit.AnswerCreateServiceResponse;
import com.wsws.moduleapplication.feed.dto.answer.edit.AnswerEditServiceRequest;
import com.wsws.moduleapplication.feed.dto.answer.edit.AnswerVisibilityEditServiceRequest;
import com.wsws.moduleapplication.feed.exception.AlreadyAnswerWrittenException;
import com.wsws.moduleapplication.feed.exception.AnswerChangeNotAllowedException;
import com.wsws.moduleapplication.feed.exception.AnswerNotFoundException;
import com.wsws.moduleapplication.user.dto.LikeServiceRequest;
import com.wsws.moduleapplication.user.exception.AlreadyLikedException;
import com.wsws.moduleapplication.user.exception.NotLikedException;
import com.wsws.moduleapplication.user.exception.ProfileImageProcessingException;
import com.wsws.moduleapplication.util.FileValidator;
import com.wsws.modulecommon.service.FileStorageService;
import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduledomain.feed.answer.repo.AnswerRepository;
import com.wsws.moduledomain.user.Like;
import com.wsws.moduledomain.user.repo.LikeRepository;
import com.wsws.moduledomain.user.vo.TargetType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final LikeRepository likeRepository;

    private final FileStorageService fileStorageService;

    /**
     * 답변 생성
     */
    public AnswerCreateServiceResponse createAnswer(AnswerCreateServiceRequest request) {

        validateAlreadyAnswerWritten(request.userId(), request.questionId()); // 이미 답변을 작성한 적이 있는 질문인지 검증

        String url = processImage(request.image()); // 이미지 처리

        // 답변 도메인 생성
        Answer answer = Answer.create(
                null,
                request.content(),
                request.visibility(),
                url,
                0,
                LocalDateTime.now(),
                request.questionId(),
                request.userId()
        );

        Answer saved = null; // 저장
        saved = answerRepository.save(answer);

        return new AnswerCreateServiceResponse(saved.getAnswerId().getValue());
    }

    /**
     * 답변 수정
     */
    public void editAnswer(AnswerEditServiceRequest request) {
        String url = processImage(request.image()); // 이미지 처리

        Answer answer = answerRepository.findById(request.answerId())
                .orElseThrow(() -> AnswerNotFoundException.EXCEPTION);

        validateChangeAuth(request.userId(), answer.getUserId().getValue()); // 수정 권한이 있는지 확인

        answer.editAnswer(request.content(), request.visibility(), url);

        try {
            answerRepository.edit(answer);
        } catch (RuntimeException e) {
            throw AnswerNotFoundException.EXCEPTION;
        }
    }

    /**
     * 답변 공개여부 수정
     */
    public void editAnswerVisibility(AnswerVisibilityEditServiceRequest request) {
        Answer answer = answerRepository.findById(request.answerId())
                .orElseThrow(() -> AnswerNotFoundException.EXCEPTION);

        validateChangeAuth(request.reqUserId(), answer.getUserId().getValue()); // 요청한 사용자가 수정하려는 답변의 작성자와 같은지 검증

        answer.changeVisibility(request.visibility()); // 공개여부 수정

        answerRepository.edit(answer); // 수정 반영
    }

    /**
     * 답변 삭제
     */
    public void deleteAnswer(Long answerId, String userId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> AnswerNotFoundException.EXCEPTION);

        validateChangeAuth(userId, answer.getUserId().getValue()); // 삭제권한이 있는지 확인

        answerRepository.deleteById(answerId);
    }

    /**
     * 좋아요 추가
     */
    public void addLikeToAnswer(LikeServiceRequest request) {

        createLike(request); // Like 객체 생성

        Answer answer = answerRepository.findById(request.targetId())
                .orElseThrow(() -> AnswerNotFoundException.EXCEPTION);

        answer.addLikeCount();// Answer의 likeCount 1증가

        // 수정 반영
        try {
            answerRepository.edit(answer);
        } catch (RuntimeException e) {
            throw AnswerNotFoundException.EXCEPTION;
        }

    }

    /**
     * 좋아요 취소
     */
    public void cancelLikeToAnswer(LikeServiceRequest request) {

        Answer answer = answerRepository.findById(request.targetId())
                .orElseThrow(() -> AnswerNotFoundException.EXCEPTION);

        deleteLike(request); // 기존 좋아요 객체 삭제

        answer.cancelLikeCount();// Answer의 likeCount 1 감소

        // 수정 반영
        answerRepository.edit(answer);
    }





    /* private 메서드 */

    // 이미지 처리
    private String processImage(MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            try {
                FileValidator.validate(image, "image");
                return fileStorageService.saveFile(image);
            } catch (Exception e) {
                throw ProfileImageProcessingException.EXCEPTION;
            }
        }
        return null; // 이미지가 없는 경우
    }
    // Like 저장 생성 및 저장

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
    // Like 삭제

    private void deleteLike(LikeServiceRequest request) {
        if (!isAlreadyLike(request.targetId(), request.userId(), TargetType.valueOf(request.targetType()))) // 좋아요를 누른적이 없다면 예외
            throw NotLikedException.EXCEPTION;

        likeRepository.deleteByTargetIdAndUserId(request.targetId(), request.userId()); // 해당 좋아요 정보 삭제
    }

    // 같은 글에 좋아요를 누른적이 있는지 확인

    private boolean isAlreadyLike(Long targetId, String userId, TargetType targetType) {
        return likeRepository.existsByTargetIdAndUserIdAndTargetType(targetId, userId, targetType);
    }

    // 수정 권한이 있는지 확인
    private void validateChangeAuth(String reqUserId, String authorId) {
        if (!reqUserId.equals(authorId)) {
            throw AnswerChangeNotAllowedException.EXCEPTION;
        }
    }

    // 이미 답변을 작성한 적이 있는 질문인지 확인
    private void validateAlreadyAnswerWritten(String userId, Long questionId) {
        if(answerRepository.existsByUserIdAndQuestionId(userId, questionId)) {
            throw AlreadyAnswerWrittenException.EXCEPTION;
        }
    }
}