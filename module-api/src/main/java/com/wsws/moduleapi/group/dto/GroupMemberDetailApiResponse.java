package com.wsws.moduleapi.group.dto;

import com.wsws.moduleapplication.group.dto.GroupMemberDetailServiceResponse;

public record GroupMemberDetailApiResponse(
        Long groupMemberId,
        String userId,
        String userNickname,
        String profileImage,
        String description
) {
    // servieResponse -> ApiResponse
    public GroupMemberDetailApiResponse(GroupMemberDetailServiceResponse groupMemberDetail) {
        this(
                groupMemberDetail.groupMemberId(),
                groupMemberDetail.userId(),
                groupMemberDetail.userNickname(),
                groupMemberDetail.profileImage(),
                groupMemberDetail.description()
        );
    }
}