package com.wsws.moduleapplication.group.dto;

import org.springframework.web.multipart.MultipartFile;

public record UpdateGroupRequest(
        String groupName,  // 변경할 그룹 이름
        String description, // 변경할 그룹 설명
        MultipartFile image
) {
}
