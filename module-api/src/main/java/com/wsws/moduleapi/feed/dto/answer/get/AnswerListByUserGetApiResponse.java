package com.wsws.moduleapi.feed.dto.answer.get;

import com.wsws.moduleapplication.feed.dto.answer.read.AnswerListFindByUserServiceResponse;

import java.util.List;

public record AnswerListByUserGetApiResponse(
        List<AnswerByUserGetApiResponse> answers
) {
    public static AnswerListByUserGetApiResponse toApiResponse(AnswerListFindByUserServiceResponse serviceResponse) {
        return new AnswerListByUserGetApiResponse(serviceResponse.answers().stream()
                .map(AnswerByUserGetApiResponse::toApiResponse)
                .toList());
    }
}
