package com.wsws.moduleapplication.feed.service;

import com.wsws.moduleapplication.feed.dto.answer_comment.AnswerCommentCreateServiceRequest;
import com.wsws.moduleapplication.feed.dto.answer_comment.AnswerCommentCreateServiceResponse;
import com.wsws.moduleapplication.feed.dto.answer_comment.AnswerCommentEditServiceRequest;
import com.wsws.moduleapplication.feed.exception.AnswerCommentChangeNotAllowedException;
import com.wsws.moduleapplication.feed.exception.AnswerCommentNotFoundException;
import com.wsws.moduleapplication.feed.exception.AnswerNotFoundException;
import com.wsws.moduleapplication.feed.exception.ParentAnswerCommentNotFoundException;
import com.wsws.moduleapplication.feed.dto.LikeServiceRequest;
import com.wsws.moduleapplication.usercontext.user.exception.AlreadyLikedException;
import com.wsws.moduleapplication.usercontext.user.exception.NotLikedException;
import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduledomain.feed.answer.repo.AnswerRepository;
import com.wsws.moduledomain.feed.comment.AnswerComment;
import com.wsws.moduledomain.feed.comment.repo.AnswerCommentRepository;
import com.wsws.moduledomain.feed.like.Like;
import com.wsws.moduledomain.feed.like.LikeRepository;
import com.wsws.moduledomain.notification.Notification;
import com.wsws.moduledomain.notification.repo.NotificationRepository;
import com.wsws.moduledomain.usercontext.user.aggregate.User;
import com.wsws.moduledomain.usercontext.user.repo.UserRepository;
import com.wsws.moduledomain.feed.like.TargetType;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import com.wsws.moduleexternalapi.fcm.dto.fcmRequestDto;
import com.wsws.moduleapplication.usercontext.user.exception.UserNotFoundException;
import com.wsws.moduleexternalapi.fcm.service.FcmService;
import com.wsws.moduleexternalapi.fcm.util.FcmType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerCommentService {

    private final AnswerCommentRepository answerCommentRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final FcmService fcmService;
    private final NotificationRepository notificationRepository;



    /**
     * 답변 댓글 추가
     */
    public AnswerCommentCreateServiceResponse createAnswerComment(AnswerCommentCreateServiceRequest request) {
        int depth = 0;
        Long parentCommentId = request.parentCommentId();
        AnswerComment parentAnswerComment = null;
        if (parentCommentId != null) {
            parentAnswerComment = answerCommentRepository.findById(parentCommentId)
                    .orElseThrow(() -> ParentAnswerCommentNotFoundException.EXCEPTION); // 부모 댓글 불러오기
            depth = parentAnswerComment.getDepth() + 1;// 부모 댓글의 depth + 1
        }
        AnswerComment answerComment = AnswerComment.create(
                null,
                request.content(),
                depth,
                0,
                LocalDateTime.now(),
                request.answerId(),
                request.userId(),
                parentCommentId
        );

        AnswerComment saved = answerCommentRepository.save(answerComment);
        // 답변 댓글 알림 전송
        sendAnswerCommentNotification(request.userId(), request.answerId());
        return new AnswerCommentCreateServiceResponse(saved.getAnswerCommentId().getValue());
    }

    /**
     * 답변 댓글 수정
     */
    public void editAnswerComment(AnswerCommentEditServiceRequest request) {

        AnswerComment answerComment = answerCommentRepository.findById(request.answerCommentId())
                .orElseThrow(() -> AnswerCommentNotFoundException.EXCEPTION);

        // 댓글의 작성자와 요청한 사용자와 다를 때
        validateEditAuth(request.userId(), answerComment);

        answerComment.editAnswerComment(request.content());

        answerCommentRepository.edit(answerComment);
    }


    /**
     * 답변 댓글 삭제
     */
    public void deleteAnswerComment(Long answerCommentId, String userId) {
        AnswerComment answerComment = answerCommentRepository.findById(answerCommentId)
                .orElseThrow(() -> AnswerCommentNotFoundException.EXCEPTION);

        validateEditAuth(userId, answerComment); // 삭제 권한 체크

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
        // 댓글 좋아요 알림 전송
        sendCommentLikeNotification(request.userId(), answerComment);
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

    // 게시글 답변 알림 전송
    private void sendAnswerCommentNotification(String commenterId, Long answerId){
        // 댓글 작성자 정보 조회
        User commenter = userRepository.findById(UserId.of(commenterId))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
        // 답변 게시글 정보 조회
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> AnswerNotFoundException.EXCEPTION);

        // 답변 작성자 정보 조회
        String answerAuthorId = answer.getUserId().getValue();
        User answerAuthor = userRepository.findById(UserId.of(answerAuthorId))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        // 알림 내용 생성
        String title = fcmService.makeFcmTitle(FcmType.ANSWER_COMMENT.getType());
        String body = fcmService.makeCommentBody(commenter.getNickname().getValue(),FcmType.ANSWER_COMMENT.getType());
        fcmRequestDto fcmDTO = fcmService.makeFcmDTO(title, body);

        // URL 생성
        String url = "/feed/answers/" + answer.getAnswerId().getValue();

        // 알림 저장
        Notification notification = Notification.create(
                null,
                FcmType.ANSWER_COMMENT.getType(),
                commenter.getNickname().getValue(),
                answerAuthor.getNickname().getValue(),
                body,
                answer.getAnswerId().getValue(), // targetId에 답변 ID 저장
                null, // commentId는 댓글 상세 조회 시 추가 가능
                null,  // 그룹 관련 없음
                url
        );
        notificationRepository.save(notification);

        // FCM 전송
        fcmService.fcmSend(answerAuthor.getNickname().getValue(), fcmDTO);
    }

    // 댓글 좋아요 알림 전송
    private void sendCommentLikeNotification(String likerId, AnswerComment answerComment) {
        // 좋아요 누른 사용자 정보 조회
        User liker = userRepository.findById(UserId.of(likerId))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        // 댓글 작성자 정보 조회
        User commentAuthor = userRepository.findById(answerComment.getUserId())
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        // 알림 내용 생성
        String title = fcmService.makeFcmTitle(FcmType.COMMENT_LIKE.getType());
        String body = fcmService.makeLikeBody(liker.getNickname().getValue(), FcmType.COMMENT_LIKE.getType());
        fcmRequestDto fcmDTO = fcmService.makeFcmDTO(title, body);

        // URL 생성
        String url = "/feed/comments/" + answerComment.getAnswerCommentId().getValue();

        // 알림 저장
        Notification notification = Notification.create(
                null,
                FcmType.COMMENT_LIKE.getType(),
                liker.getNickname().getValue(),
                commentAuthor.getNickname().getValue(),
                body,
                answerComment.getAnswerId().getValue(), // targetId에 댓글이 달린 답변 ID 저장
                answerComment.getAnswerCommentId().getValue(), // commentId에 댓글 ID 저장
                null, // 그룹과 관련 없음
                url
        );
        notificationRepository.save(notification);

        // FCM 전송
        fcmService.fcmSend(commentAuthor.getNickname().getValue(), fcmDTO);
    }

    /* private 메서드 */

    // 수정 권한 체크 메서드
    private void validateEditAuth(String userId, AnswerComment answerComment) {
        if (!answerComment.getUserId().getValue().equals(userId)) {
            throw AnswerCommentChangeNotAllowedException.EXCEPTION;
        }
    }

    private Answer getRelatedAnswer(Long answerId) {
        return answerRepository.findById(answerId)
                .orElseThrow(() -> AnswerCommentNotFoundException.EXCEPTION);
    }
}
