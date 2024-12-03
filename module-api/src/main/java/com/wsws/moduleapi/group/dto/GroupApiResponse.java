package com.wsws.moduleapi.group.dto;

import com.wsws.moduleapplication.group.dto.GroupServiceResponse;

import java.time.LocalDateTime;

public record GroupApiResponse(
        Long groupId,
        String url,
        String groupName,
        String description,
        boolean isOpen,
        LocalDateTime createdAt,
        Long membersCount
) {
    public GroupApiResponse(GroupServiceResponse serviceResponse) {
        this(
                serviceResponse.groupId(),
                serviceResponse.url(),
                serviceResponse.groupName(),
                serviceResponse.description(),
                serviceResponse.isOpen(),
                serviceResponse.createdAt(),
                serviceResponse.membersCount()
        );
    }
}
