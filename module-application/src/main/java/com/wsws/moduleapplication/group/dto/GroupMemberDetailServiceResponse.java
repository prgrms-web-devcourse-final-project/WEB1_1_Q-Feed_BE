package com.wsws.moduleapplication.group.dto;

import com.wsws.moduledomain.group.dto.GroupMemberDetailDto;
import com.wsws.moduledomain.group.dto.GroupMemberDto;

public record GroupMemberDetailServiceResponse(
        Long groupMemberId,
        String userNickname,
        String profileImage,
        String description
) {
    public GroupMemberDetailServiceResponse(GroupMemberDetailDto memberDetailDto) {
        this(
                memberDetailDto.groupMemberId(),
                memberDetailDto.userNickname(),
                memberDetailDto.profileImage(),
                memberDetailDto.description()
        );
    }
}
