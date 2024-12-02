package com.wsws.moduleinfra.repo.follow;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wsws.moduledomain.follow.QFollow;
import com.wsws.moduledomain.user.QUser;
import com.wsws.moduleinfra.repo.follow.dto.FollowResponseInfraDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FollowReadRepositoryImpl implements FollowReadRepository {

    private final JPAQueryFactory queryFactory;

    private static final QFollow follow = QFollow.follow;
    private static final QUser user = QUser.user;


    @Override
    public int countFollowersByUserId(String userId) {
        Long count = queryFactory
                .select(follow.count())
                .from(follow)
                .where(follow.followeeId.eq(userId))
                .fetchOne();
        return count != null ? count.intValue() : 0;
    }

    @Override
    public int countFollowingsByUserId(String userId) {
        Long count = queryFactory
                .select(follow.count())
                .from(follow)
                .where(follow.followerId.eq(userId))
                .fetchOne();
        return count != null ? count.intValue() : 0;
    }

    //나를 팔로우 하고 있는 사람 목록
    @Override
    public List<FollowResponseInfraDto> findFollowersWithCursor(String followeeId, LocalDateTime cursor, int size) {

        //캐싱 추가 예정

        return queryFactory
                .select(Projections.constructor(FollowResponseInfraDto.class,
                        follow.followerId.as("userId"),
                        user.nickname.as("nickname"),
                        user.profileImage.as("profileImage"),
                        follow.createdAt
                ))
                .from(follow)
                .join(user).on(user.id.value.eq(follow.followerId))
                .where(
                        follow.followeeId.eq(followeeId)
                                .and(cursor != null ? follow.createdAt.lt(cursor) : null)
                )
                .orderBy(follow.createdAt.desc())
                .limit(size)
                .fetch();

    }

    //내가 팔로우하고 있는 사람 목록
    @Override
    public List<FollowResponseInfraDto> findFollowingsWithCursor(String followerId, LocalDateTime cursor, int size) {

        //캐싱 추가 예정

        return queryFactory
                .select(Projections.constructor(FollowResponseInfraDto.class,
                        follow.followeeId.as("userId"),
                        user.nickname.as("nickname"),
                        user.profileImage.as("profileImage"),
                        follow.createdAt
                ))
                .from(follow)
                .join(user).on(user.id.value.eq(follow.followeeId))
                .where(
                        follow.followerId.eq(followerId)
                                .and(cursor != null ? follow.createdAt.lt(cursor) : null)
                )
                .orderBy(follow.createdAt.desc())
                .limit(size)
                .fetch();

    }
}
