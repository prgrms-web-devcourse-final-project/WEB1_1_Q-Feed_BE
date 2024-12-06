package com.wsws.moduleapi.group.dto;

import java.time.LocalDateTime;

public record GroupPostDetailApiResponse (
        String authorNickname,
        String authorProfile,
        String content,
        LocalDateTime createdAt,
//        boolean hasReaction,    // 좋아요 여부
        boolean isFollowing
//         int reactionCount,      // 반응 수
//        int commentCount,       // 댓글 수
//        List<GroupCommentResponseDto> comments
        ) {
//    public GroupPostDetailApiResponse(GroupPostDetailServiceResponse serviceResponse){
//        this(
//
//        )
//    }




}
