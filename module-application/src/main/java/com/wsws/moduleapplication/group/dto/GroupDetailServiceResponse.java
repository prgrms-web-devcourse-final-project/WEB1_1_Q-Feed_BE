package com.wsws.moduleapplication.group.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.wsws.moduledomain.group.dto.GroupDetailDto;
import com.wsws.moduledomain.group.dto.GroupMemberDto;


public record GroupDetailServiceResponse(
        Long groupId,
        String categoryName,
        String url,
        String groupName,
        String description,
        String adminId,
        LocalDateTime createdAt,
        List<GroupMemberServiceResponse> members
) {
    public GroupDetailServiceResponse(GroupDetailDto group, List<GroupMemberDto> members) {
        this(
                group.groupId(),
                group.categoryName().name(), // CategoryName 가져오기
                group.url(),
                group.groupName(),
                group.description(),
                group.adminId(),
                group.createdAt(),
                members.stream() // GroupMemberDto-> GroupMemberServiceResponse
                        .map(GroupMemberServiceResponse::new)
                        .collect(Collectors.toList())
        );
    }
}