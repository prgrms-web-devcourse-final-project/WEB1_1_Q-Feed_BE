package com.wsws.moduleapplication.group.dto;

import com.wsws.moduledomain.group.dto.GroupPostDetailDto;

import java.time.LocalDateTime;

public record GroupPostDetailServiceResponse (
        String authorNickName,
        String authorProfile,
        String content,
        LocalDateTime createAt
//        Integer commentCount
//        List<CommentDetailResponse> comments
){
    public GroupPostDetailServiceResponse (GroupPostDetailDto groupPost) {
        this(
                groupPost.authorNickname(),
                groupPost.authorProfile(),
                groupPost.content(),
                groupPost.createdAt()
        );
    }
}
