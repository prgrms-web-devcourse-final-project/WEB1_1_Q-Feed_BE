package com.wsws.moduledomain.feed.answer.vo;

import com.wsws.moduledomain.feed.like.LikeTargetId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AnswerId extends LikeTargetId {

    private AnswerId(Long value) {
        super(value);
    }

    public static AnswerId of(Long id) {
        return new AnswerId(id);
    }

}


