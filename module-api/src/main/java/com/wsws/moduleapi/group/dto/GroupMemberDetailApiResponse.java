package com.wsws.moduleapi.group.dto;

import com.wsws.moduleapplication.group.dto.GroupMemberDetailServiceResponse;

public record GroupMemberDetailApiResponse(
        Long groupMemberId,
        String userNickname,
        String profileImage,
        String description
) {
    // GroupMemberDto -> GroupMemberApiResponse
    public GroupMemberDetailApiResponse(GroupMemberDetailServiceResponse groupMemberDetail) {
        this(
                groupMemberDetail.groupMemberId(),
                groupMemberDetail.userNickname(),
                groupMemberDetail.profileImage(),
                groupMemberDetail.description()
        );
    }
}