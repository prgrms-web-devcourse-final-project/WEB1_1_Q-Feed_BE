package com.wsws.moduleapplication.group.service;

import com.wsws.moduleapplication.group.dto.CreateGroupPostRequest;
import com.wsws.moduleapplication.group.dto.GroupPostDetailServiceResponse;
import com.wsws.moduleapplication.group.dto.GroupPostServiceResponse;
import com.wsws.moduleapplication.feed.dto.LikeServiceRequest;
import com.wsws.moduleapplication.usercontext.user.exception.AlreadyLikedException;
import com.wsws.moduleapplication.usercontext.user.exception.NotLikedException;
import com.wsws.moduleapplication.usercontext.user.exception.ProfileImageProcessingException;
import com.wsws.moduleapplication.util.ProfileImageValidator;
import com.wsws.modulecommon.service.FileStorageService;
import com.wsws.moduledomain.group.GroupPost;
import com.wsws.moduledomain.group.dto.GroupCommentDto;
import com.wsws.moduledomain.group.dto.GroupPostDetailDto;
import com.wsws.moduledomain.group.repo.GroupCommentRepository;
import com.wsws.moduledomain.group.repo.GroupPostRepository;
import com.wsws.moduledomain.feed.like.Like;
import com.wsws.moduledomain.feed.like.LikeRepository;
import com.wsws.moduledomain.feed.like.TargetType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupPostService {
    private final GroupPostRepository groupPostRepository;
    private final FileStorageService fileStorageService;
    private final LikeRepository likeRepository;
    private final GroupCommentRepository groupCommentRepository;

    // 게시물 생성
    @Transactional
    public void createGroupPost(CreateGroupPostRequest request, Long groupId, String userId) {
        GroupPost post = GroupPost.create(
                null, groupId, request.content(),
                processGroupPostImage(request.url()), userId, 0L
        );
        groupPostRepository.save(post);
    }

    // 게시물 목록 조회
    public List<GroupPostServiceResponse> getGroupPostsList(Long groupId) {
        return groupPostRepository.findByGroupId(groupId).stream()
                .map(GroupPostServiceResponse::new)
                .toList();
    }

    // 그룹 게시물 상세조회
    @Transactional
    public GroupPostDetailServiceResponse getGroupPostDetail(Long groupPostId, String userId) {

        GroupPostDetailDto groupPostDetailDto = groupPostRepository.findByGroupPostId(groupPostId)
                .orElseThrow(() -> new IllegalArgumentException("그룹 게시물을 찾을 수 없습니다."));


        List<GroupCommentDto> comments = groupCommentRepository.findByGroupPostId(groupPostId);

        return new GroupPostDetailServiceResponse(groupPostDetailDto, comments);

    }

    // 게시물 삭제
    @Transactional
    public void deleteGroupPost(Long groupPostId) {
        groupPostRepository.findById(groupPostId)
                .ifPresentOrElse(
                        groupPost -> groupPostRepository.deleteById(groupPostId), // 엔티티 삭제
                        () -> {
                            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
                        }
                );
    }

    @Transactional
    public void addLikeToGroupPost(LikeServiceRequest request) {
        handleLikeAction(request, true); // 좋아요 추가 처리
    }

    @Transactional
    public void cancelLikeToGroupPost(LikeServiceRequest request) {
        handleLikeAction(request, false); // 좋아요 취소 처리
    }

    // 좋아요 추가/취소 처리 통합 메서드
    private void handleLikeAction(LikeServiceRequest request, boolean isAddLike) {
        GroupPost post = groupPostRepository.findById(request.targetId())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (isAddLike) {
            createLikeIfNotExists(request); // 좋아요 추가
            post.incrementLike();
        } else {
            deleteLikeIfExists(request); // 좋아요 삭제
            post.decrementLike();
        }

        groupPostRepository.edit(post); // 변경된 게시글 저장
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

    // 게시글 이미지
    private String processGroupPostImage(MultipartFile groupImageFile) {
        if (groupImageFile != null && !groupImageFile.isEmpty()) {
            try {
                ProfileImageValidator.validate(groupImageFile);
                return fileStorageService.saveFile(groupImageFile);
            } catch (Exception e) {
                throw ProfileImageProcessingException.EXCEPTION;
            }
        }
        return null;
    }

}