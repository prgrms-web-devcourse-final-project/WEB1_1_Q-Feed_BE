package com.wsws.moduledomain.socialnetwork.interest;

import com.wsws.moduledomain.category.vo.CategoryId;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class UserInterest {
    private final CategoryId categoryId;

    private UserInterest(CategoryId categoryId) {
        this.categoryId = categoryId;
    }

    public static UserInterest create(CategoryId categoryId) {
        return new UserInterest(categoryId);
    }
}

