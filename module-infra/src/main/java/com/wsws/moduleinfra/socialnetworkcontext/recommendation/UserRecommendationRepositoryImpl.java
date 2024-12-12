package com.wsws.moduleinfra.socialnetworkcontext.recommendation;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wsws.moduledomain.follow.vo.UserRecommendation;
import com.wsws.moduledomain.follow.UserRecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.wsws.moduleinfra.socialnetworkcontext.follow.entity.QFollowEntity.followEntity;
import static com.wsws.moduleinfra.socialnetworkcontext.interest.entity.QUserInterestEntity.userInterestEntity;
import static com.wsws.moduleinfra.usercontext.entity.QUserEntity.userEntity;


@Repository
@RequiredArgsConstructor
public class UserRecommendationRepositoryImpl implements UserRecommendationRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserRecommendation> findTopRecommendations(String userId, List<Long> interestCategoryIds, int limit) {
        return queryFactory
                .select(Projections.constructor(
                        UserRecommendation.class,
                        userEntity.id,
                        userEntity.nickname,
                        userEntity.profileImage,
                        followEntity.id.followerId.count().as("followerCount")
                ))
                .from(userEntity)
                .innerJoin(userInterestEntity).on(userEntity.id.eq(userInterestEntity.user.id))
                .innerJoin(followEntity).on(userEntity.id.eq(followEntity.id.followeeId))
                .where(userEntity.id.ne(userId)
                        .and(userInterestEntity.category.id.in(interestCategoryIds)))
                .groupBy(userEntity.id)
                .orderBy(followEntity.id.followerId.count().desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<UserRecommendation> findGeneralRecommendations(String userId, int limit) {
        return queryFactory
                .select(Projections.constructor(
                        UserRecommendation.class,
                        userEntity.id,
                        userEntity.nickname,
                        userEntity.profileImage,
                        followEntity.id.followerId.count().as("followerCount")
                ))
                .from(userEntity)
                .leftJoin(followEntity).on(userEntity.id.eq(followEntity.id.followeeId))
                .where(userEntity.id.ne(userId))
                .groupBy(userEntity.id)
                .orderBy(followEntity.id.followerId.count().desc())
                .limit(limit)
                .fetch();
    }
}
