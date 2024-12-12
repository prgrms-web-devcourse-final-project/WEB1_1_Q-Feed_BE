package com.wsws.moduledomain.socialnetwork.interest;

import com.wsws.moduledomain.usercontext.user.vo.UserId;

import java.util.List;

public interface UserInterestRepository {
    List<UserInterest> findByUserId(UserId userId);

    void save(UserId userId, List<UserInterest> userInterests);

    void deleteByUserId(UserId userId);

}
