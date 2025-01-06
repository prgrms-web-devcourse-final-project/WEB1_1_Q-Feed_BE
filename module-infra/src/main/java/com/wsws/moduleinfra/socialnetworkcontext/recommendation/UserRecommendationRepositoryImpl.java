package com.wsws.moduleinfra.socialnetworkcontext.recommendation;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wsws.moduledomain.socialnetwork.recommendation.Recommendation;
import com.wsws.moduledomain.socialnetwork.recommendation.UserRecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.wsws.moduleinfra.socialnetworkcontext.follow.entity.QFollowEntity.followEntity;
import static com.wsws.moduleinfra.socialnetworkcontext.interest.entity.QUserInterestEntity.userInterestEntity;
import static com.wsws.moduleinfra.usercontext.user.entity.QUserEntity.userEntity;


@Repository
@RequiredArgsConstructor
public class UserRecommendationRepositoryImpl implements UserRecommendationRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Recommendation> findTopRecommendations(String userId, List<Long> interestCategoryIds, int limit) {
        return queryFactory
                .select(Projections.constructor(
                        Recommendation.class,
                        userEntity.id,
                        userEntity.nickname,
                        userEntity.profileImage,
                        followEntity.id.followerId.count().as("followerCount")
                ))
                .from(userEntity)
                .innerJoin(userInterestEntity).on(userEntity.id.eq(userInterestEntity.userId))
                .innerJoin(followEntity).on(userEntity.id.eq(followEntity.id.followeeId))
                .where(userEntity.id.ne(userId)
                        .and(userInterestEntity.categoryId.in(interestCategoryIds)))
                .groupBy(userEntity.id)
                .orderBy(followEntity.id.followerId.count().desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<Recommendation> findGeneralRecommendations(String userId, int limit) {
        return queryFactory
                .select(Projections.constructor(
                        Recommendation.class,
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
