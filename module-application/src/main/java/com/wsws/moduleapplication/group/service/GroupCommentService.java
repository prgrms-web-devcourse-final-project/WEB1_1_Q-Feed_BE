package com.wsws.moduleapplication.group.service;


import com.wsws.moduleapplication.group.dto.CreateGroupCommentRequest;
import com.wsws.moduleapplication.feed.dto.LikeServiceRequest;
import com.wsws.moduleapplication.group.dto.GroupCommentServiceResponse;
import com.wsws.moduleapplication.usercontext.user.exception.AlreadyLikedException;
import com.wsws.moduleapplication.usercontext.user.exception.NotLikedException;
import com.wsws.moduledomain.group.GroupComment;
import com.wsws.moduledomain.group.GroupPost;
import com.wsws.moduledomain.group.repo.GroupCommentRepository;
import com.wsws.moduledomain.group.repo.GroupPostRepository;
import com.wsws.moduledomain.feed.like.Like;
import com.wsws.moduledomain.feed.like.LikeRepository;
import com.wsws.moduledomain.feed.like.TargetType;
import com.wsws.moduledomain.notification.Notification;
import com.wsws.moduledomain.notification.repo.NotificationRepository;
import com.wsws.moduledomain.usercontext.user.aggregate.User;
import com.wsws.moduledomain.usercontext.user.repo.UserRepository;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import com.wsws.moduleexternalapi.fcm.dto.fcmRequestDto;
import com.wsws.moduleexternalapi.fcm.service.FcmService;
import com.wsws.moduleexternalapi.fcm.util.FcmType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupCommentService {

    private final GroupCommentRepository groupCommentRepository;
    private final LikeRepository likeRepository;
    private final GroupPostRepository groupPostRepository;
    private final NotificationRepository notificationRepository;
    private final FcmService fcmService;
    private final UserRepository userRepository;



    // 그룹 게시글 댓글 생성
    @Transactional
    public void createGroupComment(CreateGroupCommentRequest request, Long groupPostId, String userId) {
        GroupPost groupPost = getGroupPost(groupPostId);

        GroupComment groupComment = GroupComment.create(
                null,
                request.content(),
                LocalDateTime.now(),
                userId,
                0L,
                groupPostId
        );
        groupComment.setGroupPostId(groupPostId); // GroupPost와 연관 설정

        groupCommentRepository.save(groupComment);

        // 댓글 생성 알림 전송
        sendGroupCommentNotification(userId, groupPost,groupComment.getGroupCommentId());
    }

    // 게시글 댓글 목록 조회
    public List<GroupCommentServiceResponse> getGroupCommentList(Long groupPostId) {
        return groupCommentRepository.findByGroupPostId(groupPostId).stream()
                .map(GroupCommentServiceResponse::new)
                .collect(Collectors.toList());
    }


    // 그룹 게시글 댓글 삭제 (본인 확인 추가)
    @Transactional
    public void deleteGroupComment(Long groupCommentId, String userId) {
        GroupComment groupComment = getGroupComment(groupCommentId);

        validateUser(groupComment, userId); // 본인 확인
        groupCommentRepository.deleteById(groupCommentId);
    }

    @Transactional
    public void addLikeToGroupComment(LikeServiceRequest request) {
        handleLikeAction(request, true); // 좋아요 추가 처리
    }

    @Transactional
    public void cancelLikeToGroupComment(LikeServiceRequest request) {
        handleLikeAction(request, false); // 좋아요 취소 처리

        // 좋아요 알림 전송
        sendLikeNotification(request.userId(), request.targetId());
    }

    // 좋아요 추가/취소 처리 통합 메서드
    private void handleLikeAction(LikeServiceRequest request, boolean isAddLike) {
        GroupComment comment = getGroupComment(request.targetId());

        if (isAddLike) {
            manageLike(request, true);
            comment.incrementLike();
        } else {
            manageLike(request, false);
            comment.decrementLike();
        }

        groupCommentRepository.edit(comment);
    }

    private void manageLike(LikeServiceRequest request, boolean isAddLike) {
        boolean alreadyLiked = isAlreadyLiked(request.targetId(), request.userId(), TargetType.valueOf(request.targetType()));

        if (isAddLike) {
            if (alreadyLiked) {
                throw AlreadyLikedException.EXCEPTION;
            }
            Like like = Like.create(
                    null,
                    TargetType.valueOf(request.targetType()),
                    request.targetId(),
                    request.userId()
            );
            likeRepository.save(like);
        } else {
            if (!alreadyLiked) {
                throw NotLikedException.EXCEPTION;
            }
            likeRepository.deleteByTargetIdAndUserId(request.targetId(), request.userId());
        }
    }


    // 좋아요 중복 확인
    private boolean isAlreadyLiked(Long targetId, String userId, TargetType targetType) {
        return likeRepository.existsByTargetIdAndUserIdAndTargetType(targetId, userId, targetType);
    }

    private GroupPost getGroupPost(Long groupPostId) {
        return groupPostRepository.findById(groupPostId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));
    }

    private GroupComment getGroupComment(Long groupCommentId) {
        return groupCommentRepository.findById(groupCommentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));
    }

    // 본인 여부 확인
    private void validateUser(GroupComment groupComment, String userId) {
        if (!groupComment.getUserId().equals(UserId.of(userId))) {
            throw new IllegalStateException("권한이 있는 사용자가 아닙니다. 본인의 댓글만 삭제 가능합니다.");
        }
    }

    // 댓글 작성 알림 전송
    private void sendGroupCommentNotification(String commenterId, GroupPost groupPost, Long commentId) {
        User commenter = userRepository.findById(UserId.of(commenterId))
                .orElseThrow(() -> new IllegalArgumentException("댓글 작성자를 찾을 수 없습니다."));

        User postAuthor = userRepository.findById(UserId.of(groupPost.getUserId().getValue()))
                .orElseThrow(() -> new IllegalArgumentException("게시글 작성자를 찾을 수 없습니다."));

        String title = fcmService.makeFcmTitle(FcmType.Q_SPACE_POST_COMMENT.getType());
        String body = fcmService.makeQCommentBody(commenter.getNickname().getValue(), FcmType.Q_SPACE_POST_COMMENT.getType());
        fcmRequestDto fcmDTO = fcmService.makeFcmDTO(title, body);

        String url = generateCommentUrl(groupPost.getGroupPostId(), commentId);

        Notification notification = Notification.create(
                null,
                FcmType.Q_SPACE_POST_COMMENT.getType(),
                commenter.getId().getValue(),
                postAuthor.getId().getValue(),
                body,
                groupPost.getGroupPostId(),
                commentId,
                null,
                url
        );
        notificationRepository.save(notification);

        fcmService.fcmSend(postAuthor.getNickname().getValue(), fcmDTO);
    }

    // 댓글 좋아요 알림 전송
    private void sendLikeNotification(String likerId, Long commentId) {
        // 좋아요 누른 사용자 조회
        User liker = userRepository.findById(UserId.of(likerId))
                .orElseThrow(() -> new IllegalArgumentException("좋아요를 누른 사용자를 찾을 수 없습니다."));

        // 댓글 작성자 정보 조회
        GroupComment comment = getGroupComment(commentId);
        User commentAuthor = userRepository.findById(UserId.of(comment.getUserId().getValue()))
                .orElseThrow(() -> new IllegalArgumentException("댓글 작성자를 찾을 수 없습니다."));

        // 알림 내용 생성
        String title = fcmService.makeFcmTitle(FcmType.Q_SPACE_COMMENT_LIKE.getType());
        String body = fcmService.makeQLikeBody(liker.getNickname().getValue(), FcmType.Q_SPACE_COMMENT_LIKE.getType());
        fcmRequestDto fcmDTO = fcmService.makeFcmDTO(title, body);

        // URL 생성
        String url = generateCommentUrl(comment.getGroupPostId(), commentId);

        // 알림 저장
        Notification notification = Notification.create(
                null,
                FcmType.Q_SPACE_COMMENT_LIKE.getType(),
                liker.getId().getValue(),
                commentAuthor.getId().getValue(),
                body,
                comment.getGroupPostId(),
                commentId,
                null,
                url
        );
        notificationRepository.save(notification);

        // FCM 전송
        fcmService.fcmSend(commentAuthor.getNickname().getValue(), fcmDTO);
    }

    // URL 생성 메서드
    private String generateCommentUrl(Long groupPostId, Long commentId) {
        return "/groups/posts/" + groupPostId + "#comment-" + commentId;
    }
}





