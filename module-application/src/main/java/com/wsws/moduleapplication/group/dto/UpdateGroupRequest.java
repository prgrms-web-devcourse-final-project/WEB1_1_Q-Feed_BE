package com.wsws.moduleapplication.group.dto;

import org.springframework.web.multipart.MultipartFile;

public record UpdateGroupRequest(
        String groupName,
        String description,
        MultipartFile image
) {
}
