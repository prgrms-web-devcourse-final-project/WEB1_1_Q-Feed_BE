package com.wsws.moduleapplication.group.dto;

import com.wsws.moduledomain.group.dto.GroupPostDto;

import java.time.LocalDateTime;

public record GroupPostServiceResponse (
        Long GroupPostId,
        String nickname,
        String profile,
        String content,
        LocalDateTime createAt,
        String userId,
        Long likeCount
) {
    public GroupPostServiceResponse (GroupPostDto dto) {
     this(
             dto.groupPostId(),
             dto.nickname(),
             dto.profile(),
             dto.content(),
             dto.createAt(),
             dto.userId(),
             dto.likeCount()
     );
    }
}
