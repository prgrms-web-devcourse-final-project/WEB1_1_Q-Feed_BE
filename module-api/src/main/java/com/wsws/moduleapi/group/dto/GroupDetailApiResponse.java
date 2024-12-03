package com.wsws.moduleapi.group.dto;

import com.wsws.moduleapplication.group.dto.GroupDetailServiceResponse;
import com.wsws.moduleapplication.group.dto.GroupServiceResponse;

import java.time.LocalDateTime;
import java.util.List;

public record GroupDetailApiResponse(
        Long groupId,
        Long categoryId,
        String url,
        String groupName,
        String description,
        String adminId,
        LocalDateTime createdAt,
        List<MemberApiResponse> members
) {
    public record MemberApiResponse(
            String memberId,
            String memberNickname,
            String memberProfile
    ) {
    }

    public GroupDetailApiResponse(GroupDetailServiceResponse serviceResponse) {
        this(
                serviceResponse.groupId(),
                serviceResponse.categoryId(),
                serviceResponse.url(),
                serviceResponse.groupName(),
                serviceResponse.description(),
                serviceResponse.adminId(),
                serviceResponse.createdAt(),
                serviceResponse.members().stream()
                        .map(member -> new MemberApiResponse(
                                member.memberId(),
                                member.memberNickname(),
                                member.memberProfile()
                        ))
                        .toList()
        );
    }
}