package com.wsws.moduleapplication.group.dto;

import org.springframework.web.multipart.MultipartFile;

public record CreateGroupRequest(
        String groupName,
        String description,
        Long categoryId,
        MultipartFile url,
        boolean isOpen
) {
}
