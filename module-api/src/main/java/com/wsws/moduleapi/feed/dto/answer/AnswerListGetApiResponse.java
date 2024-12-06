package com.wsws.moduleapi.feed.dto.answer;

import com.wsws.moduleapplication.feed.dto.answer.AnswerFindServiceResponse;
import com.wsws.moduleapplication.feed.dto.answer.AnswerListFindServiceResponse;

import java.util.List;

public record AnswerListGetApiResponse(
        List<AnswerGetApiResponse> answers
) {
    public static AnswerListGetApiResponse toApiResponse(AnswerListFindServiceResponse serviceResponse) {
        return new AnswerListGetApiResponse(serviceResponse.answers().stream()
                .map(AnswerGetApiResponse::toApiResponse)
                .toList());
    }
}
