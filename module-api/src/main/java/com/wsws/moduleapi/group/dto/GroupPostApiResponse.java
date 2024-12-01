package com.wsws.moduleapi.group.dto;

import com.wsws.moduleapplication.group.dto.GroupPostServiceResponse;

import java.time.LocalDateTime;

public record GroupPostApiResponse (
        Long groupPostId,
        String nickname,
        String profile,
        String content,
        LocalDateTime createAt,
        Long likeCount
//        boolean hasReaction,
//        Long reactionCount,
//        Long commentCount
) {
    public GroupPostApiResponse(GroupPostServiceResponse serviceResponse) {
        this(
                serviceResponse.GroupPostId(),
                serviceResponse.nickname(),
                serviceResponse.profile(),
                serviceResponse.content(),
                serviceResponse.createAt(),
                serviceResponse.likeCount());

    }
}