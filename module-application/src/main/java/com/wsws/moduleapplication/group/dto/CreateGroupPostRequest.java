package com.wsws.moduleapplication.group.dto;

import org.springframework.web.multipart.MultipartFile;

public record CreateGroupPostRequest(
        String content,
        MultipartFile url
){

}
