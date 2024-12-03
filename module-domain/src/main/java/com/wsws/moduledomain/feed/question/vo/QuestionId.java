package com.wsws.moduledomain.feed.question.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class QuestionId implements Serializable {

    private Long value;

    private QuestionId(Long value) {
        this.value = value;
    }

    public static QuestionId of(Long id) {
        return new QuestionId(id);
    }
}
