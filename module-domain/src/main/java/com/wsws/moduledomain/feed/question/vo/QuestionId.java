package com.wsws.moduledomain.feed.question.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class QuestionId implements Serializable {

    private Long id;

    private QuestionId(Long id) {
        this.id = id;
    }

    public static QuestionId of(Long id) {
        return new QuestionId(id);
    }
}
