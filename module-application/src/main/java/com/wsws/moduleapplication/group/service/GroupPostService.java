package com.wsws.moduleapplication.group.service;

import com.wsws.moduleapplication.group.dto.CreateGroupPostRequest;
import com.wsws.moduleapplication.group.dto.GroupPostServiceResponse;
import com.wsws.moduleapplication.user.dto.LikeServiceRequest;
import com.wsws.moduleapplication.user.exception.AlreadyLikedException;
import com.wsws.moduleapplication.user.exception.NotLikedException;
import com.wsws.moduleapplication.user.exception.ProfileImageProcessingException;
import com.wsws.moduleapplication.user.exception.UserNotFoundException;
import com.wsws.moduleapplication.util.ProfileImageValidator;
import com.wsws.modulecommon.service.FileStorageService;
import com.wsws.moduledomain.group.GroupPost;
import com.wsws.moduledomain.group.repo.GroupPostRepository;
import com.wsws.moduledomain.user.Like;
import com.wsws.moduledomain.user.User;
import com.wsws.moduledomain.user.repo.LikeRepository;
import com.wsws.moduledomain.user.repo.UserRepository;
import com.wsws.moduledomain.user.vo.TargetType;
import com.wsws.moduledomain.user.vo.UserId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupPostService {
    private final GroupPostRepository groupPostRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final LikeRepository likeRepository;


    // 게시물 생성
    @Transactional
    public void createGroupPost(CreateGroupPostRequest request, Long groupId, String userId) {
        GroupPost post = GroupPost.create(
                0L, groupId, request.content(),
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

    // 게시물 삭제
    @Transactional
    public void deleteGroupPost(Long groupPostId, String userId) {
        groupPostRepository.findById(groupPostId)
                .ifPresentOrElse(
                        groupPost -> groupPostRepository.delete(groupPostId), // 엔티티 삭제
                        () -> {
                            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
                        }
                );
    }


//    //게시물 상세 조회
//    @Transactional
//    public GroupPostDetailResponse getGroupPostDetail(Long groupPostId) {
//        GroupPostDetailDto groupPostDetailDto = groupPostRepository.findById(groupPostId)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
//        return new GroupPostDetailResponse(groupPostDetailDto);
//    }

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
        User user = userRepository.findById(UserId.of(request.userId()))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        if (isAlreadyLiked(request.targetId(), request.userId())) {
            throw AlreadyLikedException.EXCEPTION; // 이미 좋아요를 누른 경우 예외
        }

        Like like = Like.create(
                null,
                TargetType.valueOf(request.targetType()),
                request.targetId(),
                request.userId()
        );

        likeRepository.save(like, user); // 좋아요 저장
    }

    // 좋아요 삭제 처리
    private void deleteLikeIfExists(LikeServiceRequest request) {
        if (!isAlreadyLiked(request.targetId(), request.userId())) {
            throw NotLikedException.EXCEPTION; // 좋아요를 누른 적이 없는 경우 예외
        }

        likeRepository.deleteByTargetIdAndUserId(request.targetId(), request.userId()); // 좋아요 정보 삭제
    }

    // 좋아요 중복 확인
    private boolean isAlreadyLiked(Long targetId, String userId) {
        return likeRepository.existsByTargetIdAndUserId(targetId, userId);
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