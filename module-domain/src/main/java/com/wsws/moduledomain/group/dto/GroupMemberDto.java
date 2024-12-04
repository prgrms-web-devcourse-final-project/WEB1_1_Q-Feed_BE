package com.wsws.moduledomain.group.dto;

public record GroupMemberDto(
        Long groupMemberId,
        String userId,
        String userNickname,
        String userProfile
) {
}
