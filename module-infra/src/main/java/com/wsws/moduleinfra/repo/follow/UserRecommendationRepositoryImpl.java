package com.wsws.moduleinfra.repo.follow;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wsws.moduledomain.recommendation.UserRecommendation;
import com.wsws.moduledomain.recommendation.repo.UserRecommendationRepository;
import com.wsws.moduledomain.user.vo.UserInterest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.wsws.moduleinfra.entity.user.QUserEntity.userEntity;
import static com.wsws.moduleinfra.entity.follow.QFollowEntity.followEntity;
import static com.wsws.moduleinfra.entity.user.QUserInterestEntity.userInterestEntity;

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
