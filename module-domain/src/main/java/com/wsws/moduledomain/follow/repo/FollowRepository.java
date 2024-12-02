package com.wsws.moduledomain.follow.repo;

import com.wsws.moduledomain.follow.Follow;

import java.util.Optional;

public interface FollowRepository {
    Optional<Follow> findByFollowerIdAndFolloweeId(String followerId, String followeeId);
    Follow save(Follow follow);
    void delete(Follow follow);
}
