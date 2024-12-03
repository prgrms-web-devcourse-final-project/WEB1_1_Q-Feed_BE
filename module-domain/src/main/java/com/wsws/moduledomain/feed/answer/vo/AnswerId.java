package com.wsws.moduledomain.feed.answer.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class AnswerId {
    private Long value;

    private AnswerId(Long value) {
        this.value = value;
    }

    public static AnswerId of(Long id) {
        return new AnswerId(id);
    }
}
