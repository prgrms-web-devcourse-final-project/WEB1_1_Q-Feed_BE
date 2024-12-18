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
    }

    // 게시글 댓글 목록 조회
    public List<GroupCommentServiceResponse> getGroupCommentList(Long groupPostId) {
        return groupCommentRepository.findByGroupPostId(groupPostId).stream()
                .map(GroupCommentServiceResponse::new)
                .collect(Collectors.toList());
    }


    // 그룹 게시글 댓글 삭제
    @Transactional
    public void deleteGroupComment(Long groupCommentId, String userId) {
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
}





