package com.wsws.moduleapi.group.dto;

import com.wsws.moduleapplication.group.dto.GroupMemberServiceResponse;

public record GroupMemberApiResponse(
        Long groupMemberId,
        String userId,
        String userNickname,
        String userProfile
) {
    // GroupMemberDto -> GroupMemberApiResponse
    public GroupMemberApiResponse(GroupMemberServiceResponse groupMember) {
        this(
                groupMember.groupMemberId(),
                groupMember.userId(),
                groupMember.nickname(),
                groupMember.profileImage()
        );
    }
}