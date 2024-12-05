package com.wsws.moduleapplication.group.service;

import com.wsws.moduleapplication.group.dto.CreateGroupPostRequest;
import com.wsws.moduleapplication.group.dto.GroupPostServiceResponse;
import com.wsws.moduleapplication.user.exception.ProfileImageProcessingException;
import com.wsws.moduleapplication.util.ProfileImageValidator;
import com.wsws.modulecommon.service.FileStorageService;
import com.wsws.moduledomain.group.GroupPost;
import com.wsws.moduledomain.group.dto.GroupPostDto;
import com.wsws.moduledomain.group.repo.GroupPostRepository;
import com.wsws.moduledomain.user.repo.UserRepository;
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

    // 게시물 생성
    @Transactional
    public void createGroupPost(CreateGroupPostRequest request,Long groupId, String userId) {

        String groupPostImageUrl = processGroupPostImage(request.url());

        GroupPost post = GroupPost.create(
                groupId,
                request.content(),
                groupPostImageUrl,
                userId
        );

        groupPostRepository.save(post);
    }

    // 게시물 목록 조회
    public List<GroupPostServiceResponse>getGroupPostsList(Long groupId) {
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
                        () -> { throw new IllegalArgumentException("존재하지 않는 게시글입니다."); }
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


}
