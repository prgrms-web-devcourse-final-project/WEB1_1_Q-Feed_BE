package com.wsws.moduleapplication.group.dto;

import com.wsws.moduledomain.group.dto.GroupDto;

import java.time.LocalDateTime;

public record GroupServiceResponse(
        Long groupId,
        String url,
        String groupName,
        String description,
        boolean isOpen,
        LocalDateTime createdAt,
        Long membersCount
) {
    public GroupServiceResponse(GroupDto dto) {
        this(
                dto.groupId(),
                dto.url(),
                dto.groupName(),
                dto.description(),
                dto.isOpen(),
                dto.createdAt(),
                dto.membersCount()
        );
    }
}
