package com.wsws.moduledomain.group.dto;

public record GroupMemberDetailDto(
        Long groupMemberId,
        String userId,
        String userNickname,
        String profileImage,
        String description
) {
}
