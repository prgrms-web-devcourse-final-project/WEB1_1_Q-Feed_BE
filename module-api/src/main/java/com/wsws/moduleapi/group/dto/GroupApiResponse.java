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
                serviceResponse.groupId(), // groupId는 Long이기 때문에 String으로 변환
                serviceResponse.url(),
                serviceResponse.groupName(),
                serviceResponse.description(),
                serviceResponse.isOpen(),
                serviceResponse.createdAt(),
                serviceResponse.membersCount()
        );
    }
}
