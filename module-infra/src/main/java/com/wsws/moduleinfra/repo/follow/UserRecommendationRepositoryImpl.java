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
    public List<UserRecommendation> findTopRecommendations(String userId, List<UserInterest> userInterests, int limit) {

        List<Long> interestCategoryIds = userInterests.stream()
                .map(userInterest -> userInterest.getCategoryId().getValue())
                .toList();

        List<UserRecommendation> commonInterestUsers = queryFactory
                .select(Projections.constructor( // UserRecomendation vo 형식으로 projection
                        UserRecommendation.class,
                        userEntity.id,
                        userEntity.nickname,
                        userEntity.profileImage,
                        followEntity.id.followerId.count().as("followerCount")
                ))
                .from(userEntity)
                .leftJoin(userInterestEntity).on(userEntity.id.eq(userInterestEntity.user.id))
                .leftJoin(followEntity).on(userEntity.id.eq(followEntity.id.followeeId))
                .where(userEntity.id.ne(userId)
                        .and(userInterestEntity.category.id.in(interestCategoryIds)))
                .groupBy(userEntity.id)
                .orderBy(followEntity.id.followerId.count().desc())
                .limit(limit)
                .fetch();
        if (commonInterestUsers.size() < limit) { // 동일한 사람이 다 안채워지면 유사도 높은 사람
            int remaining = limit - commonInterestUsers.size();
            List<UserRecommendation> generalUsers = queryFactory
                    .select(Projections.constructor(
                            UserRecommendation.class,
                            userEntity.id,
                            userEntity.nickname,
                            userEntity.profileImage,
                            queryFactory
                                    .select(followEntity.id.followerId.count())
                                    .from(followEntity)
                                    .where(followEntity.id.followeeId.eq(userEntity.id))
                    ))
                    .from(userEntity)
                    .where(userEntity.id.ne(userId)
                            .and(userEntity.id.notIn(
                                    commonInterestUsers.stream()
                                            .map(UserRecommendation::getUserId)
                                            .toList())))
                    .orderBy(Expressions.asNumber(
                            queryFactory
                                    .select(followEntity.id.followerId.count())
                                    .from(followEntity)
                                    .where(followEntity.id.followeeId.eq(userEntity.id))
                    ).desc())
                    .limit(remaining)
                    .fetch();

            commonInterestUsers.addAll(generalUsers);
        }

        return commonInterestUsers;
    }
}
