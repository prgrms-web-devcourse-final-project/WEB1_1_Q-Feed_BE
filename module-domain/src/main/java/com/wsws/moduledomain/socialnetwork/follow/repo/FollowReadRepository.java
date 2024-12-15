package com.wsws.moduledomain.socialnetwork.follow.repo;

import com.wsws.moduledomain.socialnetwork.follow.vo.FollowQueryResult;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface FollowReadRepository {

    int countFollowersByUserId(String userId);

    int countFollowingsByUserId(String userId);
    //나를 팔로우 하고 있는 사람 목록
    List<FollowQueryResult> findFollowersWithCursor(String followeeId, LocalDateTime cursor, int size);
    //내가 팔로우하고 있는 사람 목록
    List<FollowQueryResult> findFollowingsWithCursor(String followerId, LocalDateTime cursor, int size);

    public Map<String, Long> getFollowerCounts(List<String> userIds);
}
