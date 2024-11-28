package com.wsws.moduleinfra.repo.follow;

import com.wsws.moduleinfra.repo.follow.dto.FollowResponseInfraDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FollowReadRepositoryImpl implements FollowReadRepository {


    @Override
    public int countFollowersByUserId(String userId) {
        return 0;
    }

    @Override
    public int countFollowingsByUserId(String userId) {
        return 0;
    }

    //나를 팔로우 하고 있는 사람 목록
    @Override
    public List<FollowResponseInfraDto> findFollowersWithCursor(String followeeId, String cursor, int size) {
        return List.of();
    }

    //내가 팔로우하고 있는 사람 목록
    @Override
    public List<FollowResponseInfraDto> findFollowingsWithCursor(String followerId, String cursor, int size) {
        return List.of();
    }
}
