package com.wsws.moduleinfra.socialnetworkcontext.interest.repo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wsws.moduledomain.socialnetwork.interest.UserInterestRepository;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import com.wsws.moduledomain.socialnetwork.interest.UserInterest;
import com.wsws.moduleinfra.entity.CategoryEntity;
import com.wsws.moduleinfra.usercontext.user.entity.UserEntity;
import com.wsws.moduleinfra.socialnetworkcontext.interest.entity.UserInterestEntity;
import com.wsws.moduleinfra.socialnetworkcontext.interest.entity.UserInterestEntityMapper;
import com.wsws.moduleinfra.repo.category.CategoryJpaRepository;
import com.wsws.moduleinfra.usercontext.user.repo.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.wsws.moduleinfra.socialnetworkcontext.interest.entity.QUserInterestEntity.userInterestEntity;


@Repository
@RequiredArgsConstructor
public class UserInterestRepositoryImpl implements UserInterestRepository {


    private final JPAQueryFactory queryFactory;
    private final JpaUserInterestRepository jpaUserInterestRepository;
    private final CategoryJpaRepository categoryJpaRepository;
    private final JpaUserRepository jpaUserRepository;
    private final UserInterestEntityMapper mapper;

    @Override
    public List<UserInterest> findByUserId(UserId userId) {
        // QueryDSL로 조회
        List<UserInterestEntity> entities = queryFactory.selectFrom(userInterestEntity)
                .where(userInterestEntity.user.id.eq(userId.getValue()))
                .fetch();

        return mapper.toDomainList(entities);
    }

    @Override
    public void save(UserId userId, List<UserInterest> userInterests) {
        UserEntity userEntity = jpaUserRepository.findById(userId.getValue())
                .orElse(null);

        // CategoryEntity 리스트 조회
        List<CategoryEntity> categoryEntities = categoryJpaRepository.findAllById(
                userInterests.stream().map(i -> i.getCategoryId().getValue()).toList()
        );

        // UserInterestEntity 리스트 생성
        List<UserInterestEntity> newEntities = mapper.toEntityList(userEntity, categoryEntities);

        // 새로운 관심사 저장
        jpaUserInterestRepository.saveAll(newEntities);
    }


    @Override
    public void deleteByUserId(UserId userId) {
        queryFactory.delete(userInterestEntity)
                .where(userInterestEntity.user.id.eq(userId.getValue()))
                .execute();
    }

    @Override
    public List<String> findUserIdsByInterestCategories(List<Long> categoryIds) {
        return queryFactory
                .select(userInterestEntity.user.id)
                .from(userInterestEntity)
                .where(userInterestEntity.category.id.in(categoryIds))
                .distinct()
                .fetch();
    }
}
