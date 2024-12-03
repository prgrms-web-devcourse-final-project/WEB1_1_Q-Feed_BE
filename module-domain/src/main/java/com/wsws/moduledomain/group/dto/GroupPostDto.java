package com.wsws.moduledomain.group.dto;

import com.wsws.moduledomain.group.vo.GroupId;

import java.time.LocalDateTime;

public record GroupPostDto(
        Long groupPostId,
        String content,
        LocalDateTime createdAt,
        GroupId groupId,
        String userId,
        String url,
        long likeCount
) {
}
