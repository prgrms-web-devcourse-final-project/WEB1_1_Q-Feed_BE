package com.wsws.moduleapplication.group.service;


import com.wsws.moduleapplication.group.dto.CreateGroupCommentRequest;
import com.wsws.moduledomain.group.GroupComment;
import com.wsws.moduledomain.group.GroupPost;
import com.wsws.moduledomain.group.repo.GroupCommentRepository;
import com.wsws.moduledomain.group.repo.GroupPostRepository;
import com.wsws.moduledomain.user.repo.LikeRepository;
import com.wsws.moduledomain.user.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GroupCommentService {

    private final UserRepository userRepository;
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


    // 그룹 게시글 댓글 좋아요 추가

    // 그룹 게시글 댓글 좋아요 취소

    // 그룹 게시글 댓글 삭제
}
