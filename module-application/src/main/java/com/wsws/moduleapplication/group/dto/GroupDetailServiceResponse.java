package com.wsws.moduleapplication.group.dto;

import java.time.LocalDateTime;
import java.util.List;

public record GroupDetailServiceResponse(
        Long groupId,
        Long categoryId,
        String url,
        String groupName,
        String description,
        String adminId,
        LocalDateTime createdAt,
        List<MemberResponse> members
) {
    public record MemberResponse(
            String memberId,
            String memberNickname,
            String memberProfile
    ) {}
}
