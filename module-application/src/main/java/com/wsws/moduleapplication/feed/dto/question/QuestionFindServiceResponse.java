package com.wsws.moduleapplication.feed.dto.question;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.wsws.moduledomain.feed.question.Question;
import java.time.LocalDate;

public record QuestionFindServiceResponse(
        Long questionId,
        String content,
        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonDeserialize(using = LocalDateDeserializer.class)
        LocalDate questionDate
) {

    public QuestionFindServiceResponse(Question question) {
        this(
                question.getQuestionId().getValue(),
                question.getContent(),
                question.getQuestionDate()
        );
    }
}
