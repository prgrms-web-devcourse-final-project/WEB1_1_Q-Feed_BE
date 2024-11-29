package com.wsws.moduleinfra.repo.follow;

import com.wsws.moduleinfra.repo.follow.dto.FollowResponseInfraDto;

import java.time.LocalDateTime;
import java.util.List;

public interface FollowReadRepository {

    int countFollowersByUserId(String userId);

    int countFollowingsByUserId(String userId);
    //나를 팔로우 하고 있는 사람 목록
    List<FollowResponseInfraDto> findFollowersWithCursor(String followeeId, LocalDateTime cursor, int size);
    //내가 팔로우하고 있는 사람 목록
    List<FollowResponseInfraDto> findFollowingsWithCursor(String followerId, LocalDateTime cursor, int size);
}
