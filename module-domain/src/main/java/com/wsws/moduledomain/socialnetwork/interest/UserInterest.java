package com.wsws.moduledomain.socialnetwork.interest;

import com.wsws.moduledomain.category.vo.CategoryId;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class UserInterest {
    private final CategoryId categoryId;

    private final UserId userId;

    private UserInterest(CategoryId categoryId, UserId userId) {
        this.categoryId = categoryId;
        this.userId = userId;
    }

    public static UserInterest create(CategoryId categoryId, UserId userId) {
        return new UserInterest(categoryId, userId);
    }
}

