package com.wsws.moduleapplication.group.dto;

import com.wsws.moduledomain.group.dto.GroupMemberDetailDto;

public record GroupMemberDetailServiceResponse(
        Long groupMemberId,
        String userId,
        String userNickname,
        String profileImage,
        String description
) {
    public GroupMemberDetailServiceResponse(GroupMemberDetailDto memberDetailDto) {
        this(
                memberDetailDto.groupMemberId(),
                memberDetailDto.userId(),
                memberDetailDto.userNickname(),
                memberDetailDto.profileImage(),
                memberDetailDto.description()
        );
    }
}
