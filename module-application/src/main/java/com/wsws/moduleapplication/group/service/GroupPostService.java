package com.wsws.moduleapplication.group.service;

import com.wsws.moduleapplication.feed.exception.AnswerNotFoundException;
import com.wsws.moduleapplication.group.dto.CreateGroupPostRequest;
import com.wsws.moduleapplication.group.dto.GroupPostServiceResponse;
import com.wsws.moduleapplication.user.dto.LikeServiceRequest;
import com.wsws.moduleapplication.user.exception.AlreadyLikedException;
import com.wsws.moduleapplication.user.exception.NotLikedException;
import com.wsws.moduleapplication.user.exception.ProfileImageProcessingException;
import com.wsws.moduleapplication.user.exception.UserNotFoundException;
import com.wsws.moduleapplication.util.ProfileImageValidator;
import com.wsws.modulecommon.service.FileStorageService;
import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduledomain.group.GroupPost;
import com.wsws.moduledomain.group.dto.GroupPostDto;
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

        String groupPostImageUrl = processGroupPostImage(request.url());

        GroupPost post = GroupPost.create(
                0L,
                groupId,
                request.content(),
                groupPostImageUrl,
                userId,
                0L
        );

        groupPostRepository.save(post);
    }

    // 게시물 목록 조회
    public List<GroupPostServiceResponse> getGroupPostsList(Long groupId) {
        List<GroupPostDto> posts = groupPostRepository.findByGroupId(groupId);
        return posts.stream()
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

//    //게시물 상세 조회
//    @Transactional
//    public GroupPostDetailResponse getGroupPostDetail(Long groupPostId) {
//        GroupPostDetailDto groupPostDetailDto = groupPostRepository.findById(groupPostId)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
//        return new GroupPostDetailResponse(groupPostDetailDto);
//    }

    // 게시글 좋아요 추가
    @Transactional
    public void addLikeToGroupPost(LikeServiceRequest request) {

        createLike(request);

        GroupPost post = groupPostRepository.findById(request.targetId())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        post.incrementLike();

       groupPostRepository.edit(post);

    }

    //좋아요 취소
    @Transactional
    public void cancelLikeToGroupPost(LikeServiceRequest request) {

         GroupPost post = groupPostRepository.findById(request.targetId())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        deleteLike(request);

        post.decrementLike();
        groupPostRepository.edit(post);

    }

    // 좋아요 생성
    private void createLike(LikeServiceRequest request) {

        User user = userRepository.findById(UserId.of(request.userId()))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);// 연관 맺을 User 찾아오기

        if (isAlreadyLike(request.targetId(), request.userId())) // 좋아요를 누른적이 있다면 예외
            throw AlreadyLikedException.EXCEPTION;


        Like like = Like.create(
                null,
                TargetType.valueOf(request.targetType()),
                request.targetId(),
                request.userId()
        );
        try {
            likeRepository.save(like, user);
        } catch (RuntimeException e) {
            throw UserNotFoundException.EXCEPTION;
        }

    }

    // 좋아요 삭제
    private void deleteLike (LikeServiceRequest request){
        if (!isAlreadyLike(request.targetId(), request.userId())) // 좋아요를 누른적이 없다면 예외
            throw NotLikedException.EXCEPTION;

        likeRepository.deleteByTargetIdAndUserId(request.targetId(), request.userId()); // 해당 좋아요 정보 삭제
    }

    // 좋아요 중복 확인
    private boolean isAlreadyLike(Long targetId, String userId) {
        return likeRepository.existsByTargetIdAndUserId(targetId, userId);
    }
}
