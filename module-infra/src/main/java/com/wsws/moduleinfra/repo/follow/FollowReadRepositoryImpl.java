package com.wsws.moduleinfra.repo.follow;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.wsws.moduledomain.follow.repo.FollowReadRepository;
import com.wsws.moduledomain.follow.vo.FollowQueryResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.wsws.moduleinfra.entity.follow.QFollowEntity.*;
import static com.wsws.moduleinfra.entity.user.QUserEntity.*;


@Repository
@RequiredArgsConstructor
public class FollowReadRepositoryImpl implements FollowReadRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FollowQueryResult> findFollowersWithCursor(String followeeId, LocalDateTime cursor, int size) {
        LocalDateTime effectiveCursor = cursor != null ? cursor : LocalDateTime.now();

        return queryFactory.select(Projections.constructor(
                        FollowQueryResult.class,
                        followEntity.id.followerId,
                        userEntity.nickname,
                        userEntity.profileImage,
                        followEntity.createdAt
                ))
                .from(followEntity)
                .join(userEntity).on(userEntity.id.eq(followEntity.id.followerId))
                .where(followEntity.id.followeeId.eq(followeeId)
                        .and(followEntity.createdAt.lt(effectiveCursor)))
                .orderBy(followEntity.createdAt.desc())
                .limit(size)
                .fetch();
    }

    @Override
    public List<FollowQueryResult> findFollowingsWithCursor(String followerId, LocalDateTime cursor, int size) {
        LocalDateTime effectiveCursor = cursor != null ? cursor : LocalDateTime.now();

        return queryFactory.select(Projections.constructor(
                        FollowQueryResult.class,
                        followEntity.id.followeeId,
                        userEntity.nickname,
                        userEntity.profileImage,
                        followEntity.createdAt
                ))
                .from(followEntity)
                .join(userEntity).on(userEntity.id.eq(followEntity.id.followeeId))
                .where(followEntity.id.followerId.eq(followerId)
                        .and(followEntity.createdAt.lt(effectiveCursor)))
                .orderBy(followEntity.createdAt.desc())
                .limit(size)
                .fetch();
    }

    @Override
    public int countFollowersByUserId(String userId) {
        return (int) queryFactory.selectFrom(followEntity)
                .where(followEntity.id.followeeId.eq(userId))
                .fetchCount();
    }

    @Override
    public int countFollowingsByUserId(String userId) {
        return (int) queryFactory.selectFrom(followEntity)
                .where(followEntity.id.followerId.eq(userId))
                .fetchCount();
    }
}