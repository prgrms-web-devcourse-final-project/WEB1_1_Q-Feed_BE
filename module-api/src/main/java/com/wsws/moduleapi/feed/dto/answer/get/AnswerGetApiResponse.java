package com.wsws.moduleapi.feed.dto.answer.get;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wsws.moduleapplication.feed.dto.answer.AnswerCommentFindServiceResponse;
import com.wsws.moduleapplication.feed.dto.answer.AnswerFindServiceResponse;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL) // null인 필드는 제외
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
