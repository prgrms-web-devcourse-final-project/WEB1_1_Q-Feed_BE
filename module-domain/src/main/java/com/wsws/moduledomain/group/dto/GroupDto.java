package com.wsws.moduledomain.group.dto;

import java.time.LocalDateTime;

public record GroupDto(
        Long groupId,
        String url,
        String groupName,
        String description,
        boolean isOpen,
        LocalDateTime createdAt,
        Long membersCount
) {
}
