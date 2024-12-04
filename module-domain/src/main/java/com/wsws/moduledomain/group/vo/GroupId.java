package com.wsws.moduledomain.group.vo;

import com.wsws.moduledomain.feed.question.vo.QuestionId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class GroupId {

    private Long value;

    private GroupId(Long value) {
        this.value = value;
    }

    public static GroupId of(Long id) {
        return new GroupId(id);
    }
}
