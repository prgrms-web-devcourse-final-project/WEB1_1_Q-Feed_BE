package com.wsws.moduleapplication.feed.service;

import com.wsws.moduleapplication.feed.dto.answer.*;
import com.wsws.moduleapplication.feed.exception.AnswerNotFoundException;
import com.wsws.moduleapplication.feed.exception.QuestionNotFoundException;
import com.wsws.moduleapplication.user.dto.LikeServiceRequest;
import com.wsws.moduleapplication.user.exception.AlreadyLikedException;
import com.wsws.moduleapplication.user.exception.NotLikedException;
import com.wsws.moduleapplication.user.exception.ProfileImageProcessingException;
import com.wsws.moduleapplication.user.exception.UserNotFoundException;
import com.wsws.moduleapplication.util.FileValidator;
import com.wsws.modulecommon.service.FileStorageService;
import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduledomain.feed.answer.repo.AnswerRepository;
import com.wsws.moduledomain.feed.question.Question;
import com.wsws.moduledomain.feed.question.repo.QuestionRepository;
import com.wsws.moduledomain.user.Like;
import com.wsws.moduledomain.user.User;
import com.wsws.moduledomain.user.repo.LikeRepository;
import com.wsws.moduledomain.user.repo.UserRepository;
import com.wsws.moduledomain.user.vo.TargetType;
import com.wsws.moduledomain.user.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    private final FileStorageService fileStorageService;

    public AnswerFindServiceResponse findAnswerByAnswerId(Long answerId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> AnswerNotFoundException.EXCEPTION);

        return null;
    }

    /**
     * 답변 생성
     */
    public AnswerCreateServiceResponse createAnswer(AnswerCreateServiceRequest request) {
        String url = processImage(request.image()); // 이미지 처리

        Question question = questionRepository.findById(request.questionId())
                .orElseThrow(() -> QuestionNotFoundException.EXCEPTION); // 의존관계가 있는 Question을 불러옴

        // 답변 도메인 생성
        Answer answer = Answer.create(
                null,
                request.content(),
                request.visibility(),
                url,
                0,
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

        answer.editAnswer(request.content(), request.visibility(), url);

        try {
            answerRepository.edit(answer);
        } catch (RuntimeException e) {
            throw AnswerNotFoundException.EXCEPTION;
        }
    }

    /**
     * 답변 삭제
     */
    public void deleteAnswer(Long answerId) {
        answerRepository.deleteById(answerId);
    }

    /**
     * 좋아요 추가
     */
    public void addLikeToAnswer(LikeServiceRequest request) {

        createLike(request); // Like 객체 생성

        Answer answer = answerRepository.findById(request.targetId())
                .orElseThrow(() -> AnswerNotFoundException.EXCEPTION);

        answer.addReactionCount();// Answer의 reactionCount 1증가

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

        answer.cancelReactionCount();// Answer의 reactionCount 1 감소

        // 수정 반영
        answerRepository.edit(answer);
    }




    /* private 메서드 */

    /**
     * 이미지 처리
     */
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