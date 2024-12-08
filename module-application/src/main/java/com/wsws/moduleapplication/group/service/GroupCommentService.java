package com.wsws.moduleapplication.group.service;


import com.wsws.moduleapplication.group.dto.CreateGroupCommentRequest;
import com.wsws.moduleapplication.user.dto.LikeServiceRequest;
import com.wsws.moduleapplication.user.exception.AlreadyLikedException;
import com.wsws.moduleapplication.user.exception.NotLikedException;
import com.wsws.moduledomain.group.GroupComment;
import com.wsws.moduledomain.group.GroupPost;
import com.wsws.moduledomain.group.repo.GroupCommentRepository;
import com.wsws.moduledomain.group.repo.GroupPostRepository;
import com.wsws.moduledomain.user.Like;
import com.wsws.moduledomain.user.repo.LikeRepository;
import com.wsws.moduledomain.user.repo.UserRepository;
import com.wsws.moduledomain.user.vo.TargetType;
import com.wsws.moduledomain.user.vo.UserId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GroupCommentService {

    private final GroupCommentRepository groupCommentRepository;
    private final LikeRepository likeRepository;
    private final GroupPostRepository groupPostRepository;


    // 그룹 게시글 댓글 생성

    @Transactional
    public void createGroupComment(CreateGroupCommentRequest request, Long groupPostId, String userId) {

        GroupPost groupPost = groupPostRepository.findById(groupPostId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

        GroupComment groupComment = GroupComment.create(
                null,
                request.content(),
                LocalDateTime.now(),
                userId,
                0L
        );
        groupComment.setGroupPostId(groupPostId); // GroupPost와 연관 설정

        groupCommentRepository.save(groupComment);
    }

    // 그룹 게시글 댓글 삭제
    @Transactional
    public void deleteGroupComment(Long groupCommentId, String userId) {

        GroupComment groupComment = groupCommentRepository.findById(groupCommentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));

//        // 디버깅용 로그 추가
//        System.out.println("댓글 작성자 ID (groupComment.getUserId()): " + groupComment.getUserId());
//        System.out.println("로그인된 사용자 ID (UserId.of(userId)): " + UserId.of(userId));

        // 작성자 ID 비교 //수정 필요
        if (!groupComment.getUserId().equals(UserId.of(userId))) {
            throw new IllegalStateException("권한이 없습니다. 댓글 작성자만 삭제할 수 있습니다.");
        }

        // 댓글 삭제
        groupCommentRepository.deleteById(groupCommentId);
    }

    @Transactional
    public void addLikeToGroupComment(LikeServiceRequest request) {
        handleLikeAction(request, true); // 좋아요 추가 처리
    }

    @Transactional
    public void cancelLikeToGroupComment(LikeServiceRequest request) {
        handleLikeAction(request, false); // 좋아요 취소 처리
    }

    // 좋아요 추가/취소 처리 통합 메서드
    private void handleLikeAction(LikeServiceRequest request, boolean isAddLike) {
        GroupComment comment = groupCommentRepository.findById(request.targetId())
                .orElseThrow(() -> new IllegalArgumentException("게시글 댓글을 찾을 수 없습니다."));

        if (isAddLike) {
            createLikeIfNotExists(request);
            comment.incrementLike();
        } else {
            deleteLikeIfExists(request);
            comment.decrementLike();
        }

        groupCommentRepository.edit(comment); // 변경된 게시글 저장
    }

    // 좋아요 생성 처리
    private void createLikeIfNotExists(LikeServiceRequest request) {

        if (isAlreadyLiked(request.targetId(), request.userId(), TargetType.valueOf(request.targetType()))) {
            throw AlreadyLikedException.EXCEPTION; // 이미 좋아요를 누른 경우 예외
        }

        Like like = Like.create(
                null,
                TargetType.valueOf(request.targetType()),
                request.targetId(),
                request.userId()
        );

        likeRepository.save(like); // 좋아요 저장
    }

    // 좋아요 삭제 처리
    private void deleteLikeIfExists(LikeServiceRequest request) {
        if (!isAlreadyLiked(request.targetId(), request.userId(), TargetType.valueOf(request.targetType()))) {
            throw NotLikedException.EXCEPTION; // 좋아요를 누른 적이 없는 경우 예외
        }

        likeRepository.deleteByTargetIdAndUserId(request.targetId(), request.userId()); // 좋아요 정보 삭제
    }

    // 좋아요 중복 확인
    private boolean isAlreadyLiked(Long targetId, String userId, TargetType targetType) {
        return likeRepository.existsByTargetIdAndUserIdAndTargetType(targetId, userId, targetType);
    }
}





