package com.wsws.moduledomain.user.repo;

import com.wsws.moduledomain.category.vo.CategoryId;
import com.wsws.moduledomain.user.vo.UserId;
import com.wsws.moduledomain.user.vo.UserInterest;

import java.util.List;

public interface UserInterestRepository {
    List<UserInterest> findByUserId(UserId userId);

    void save(UserId userId, List<UserInterest> userInterests);

    void deleteByUserId(UserId userId);

}
