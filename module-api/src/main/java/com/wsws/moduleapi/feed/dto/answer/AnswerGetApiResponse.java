package com.wsws.moduleapi.feed.dto.answer;

import com.wsws.moduleapplication.feed.dto.answer.AnswerCommentFindServiceResponse;
import com.wsws.moduleapplication.feed.dto.answer.AnswerFindServiceResponse;

import java.time.LocalDateTime;
import java.util.List;

public record AnswerGetApiResponse(
        Long answerId,
        String authorUserId,
        String authorNickname,
        String profileImage,
        String content,
        LocalDateTime createdAt,
        int likeCount,
        Boolean isLike,
        Boolean isFollowing,
        int commentCount,
        List<AnswerCommentFindServiceResponse> comments
) {
    public static AnswerGetApiResponse toApiResponse(AnswerFindServiceResponse serviceResponse) {
        return new AnswerGetApiResponse(
                serviceResponse.answerId(),
                serviceResponse.authorUserId(),
                serviceResponse.authorNickname(),
                serviceResponse.profileImage(),
                serviceResponse.content(),
                serviceResponse.createdAt(),
                serviceResponse.likeCount(),
                serviceResponse.isLike(),
                serviceResponse.isFollowing(),
                serviceResponse.commentCount(),
                serviceResponse.comments()
        );
    }

}
