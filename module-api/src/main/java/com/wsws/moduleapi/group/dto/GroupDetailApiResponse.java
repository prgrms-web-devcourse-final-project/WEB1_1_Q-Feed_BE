package com.wsws.moduleapi.group.dto;

import com.wsws.moduleapplication.group.dto.GroupDetailServiceResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record GroupDetailApiResponse(
        Long groupId,
        String categoryName,
        String url,
        String groupName,
        String description,
        String adminId,
        LocalDateTime createdAt,
        Boolean isOpen,
        List<GroupMemberApiResponse> members,
        List<GroupPostApiResponse> posts
) {
    public GroupDetailApiResponse(GroupDetailServiceResponse serviceResponse) {
        this(
                serviceResponse.groupId(),
                serviceResponse.categoryName(),
                serviceResponse.url(),
                serviceResponse.groupName(),
                serviceResponse.description(),
                serviceResponse.adminId(),
                serviceResponse.createdAt(),
                serviceResponse.isOpen(),
                serviceResponse.members().stream() // GroupMemberServiceResponse ->GroupMemberApiResponse
                        .map(GroupMemberApiResponse::new)
                        .collect(Collectors.toList()),
                serviceResponse.posts().stream()
                        .map(GroupPostApiResponse::new)
                        .collect(Collectors.toList())
        );
    }
}
