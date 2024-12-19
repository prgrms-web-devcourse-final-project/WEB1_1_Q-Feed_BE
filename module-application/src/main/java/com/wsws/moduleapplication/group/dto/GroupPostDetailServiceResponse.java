package com.wsws.moduleapplication.group.dto;

import com.wsws.moduledomain.group.dto.GroupCommentDto;
import com.wsws.moduledomain.group.dto.GroupPostDetailDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record GroupPostDetailServiceResponse (
        Long groupPostId,
        String nickname,
        String profile,
        String content,
        LocalDateTime createAt,
        String userId,
        Long likeCount,
        List<GroupCommentServiceResponse>comments
){
    public GroupPostDetailServiceResponse (GroupPostDetailDto groupPost, List<GroupCommentDto> comments) {
        this(
                groupPost.groupPostId(),
                groupPost.nickname(),
                groupPost.profile(),
                groupPost.content(),
                groupPost.createAt(),
                groupPost.userId(),
                groupPost.likeCount(),
                comments.stream()
                        .map(GroupCommentServiceResponse::new)
                        .collect(Collectors.toList())
        );
    }
}
