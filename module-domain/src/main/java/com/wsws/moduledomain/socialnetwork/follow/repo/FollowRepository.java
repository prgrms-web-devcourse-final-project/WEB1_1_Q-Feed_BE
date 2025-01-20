package com.wsws.moduledomain.socialnetwork.follow.repo;

import com.wsws.moduledomain.socialnetwork.follow.aggregate.Follow;

import java.util.List;
import java.util.Optional;

public interface FollowRepository {
    Optional<Follow> findByFollowerIdAndFolloweeId(String followerId, String followeeId);
    List<Follow> findByFollowerId(String followerId);
    Follow save(Follow follow);
    void delete(Follow follow);
}
