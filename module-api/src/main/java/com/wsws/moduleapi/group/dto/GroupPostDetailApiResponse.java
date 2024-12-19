package com.wsws.moduleapi.group.dto;

import com.wsws.moduleapplication.group.dto.GroupPostDetailServiceResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record GroupPostDetailApiResponse (
        Long groupPostId,
        String nickname,
        String profile,
        String content,
        LocalDateTime createAt,
        String userId,
        Long likeCount,
        List<GroupCommentApiResponse> comments
        ) {

        public GroupPostDetailApiResponse(GroupPostDetailServiceResponse response) {
                this(
                        response.groupPostId(),
                        response.nickname(),
                        response.profile(),
                        response.content(),
                        response.createAt(),
                        response.userId(),
                        response.likeCount(),
                        response.comments().stream()
                                .map(GroupCommentApiResponse::new)
                                .collect(Collectors.toList())
                );
        }





}
