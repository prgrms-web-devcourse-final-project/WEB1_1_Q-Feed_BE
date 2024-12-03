package com.wsws.moduleapplication.group.dto;

import com.wsws.moduledomain.group.dto.GroupMemberDto;

public record GroupMemberServiceResponse(
        Long groupMemberId,
        String userId,
        String nickname,
        String profileImage
) {
    public GroupMemberServiceResponse(GroupMemberDto memberDto) {
        this(
                memberDto.groupMemberId(),
                memberDto.userId(),
                memberDto.userNickname(),
                memberDto.userProfile()
        );
    }
}
