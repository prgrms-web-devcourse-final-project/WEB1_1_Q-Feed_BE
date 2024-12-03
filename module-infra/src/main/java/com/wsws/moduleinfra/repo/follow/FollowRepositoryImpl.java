package com.wsws.moduleinfra.repo.follow;


import com.wsws.moduledomain.follow.Follow;
import com.wsws.moduledomain.follow.repo.FollowRepository;
import com.wsws.moduleinfra.entity.follow.FollowEntity;
import com.wsws.moduleinfra.entity.follow.FollowEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepository {

    private final JpaFollowRepository jpaFollowRepository;

    @Override
    public Optional<Follow> findByFollowerIdAndFolloweeId(String followerId, String followeeId) {
        return jpaFollowRepository.findById_FollowerIdAndId_FolloweeId(followerId, followeeId)
                .map(FollowEntityMapper::toFollow); // 엔티티를 도메인 객체로 변환
    }

    @Override
    public Follow save(Follow follow) {
        FollowEntity entity = FollowEntityMapper.fromFollow(follow); // 도메인 객체를 엔티티로 변환
        FollowEntity savedEntity = jpaFollowRepository.save(entity); // 저장 후 엔티티 반환
        return FollowEntityMapper.toFollow(savedEntity); // 엔티티를 도메인 객체로 변환하여 반환
    }

    @Override
    public void delete(Follow follow) {
        FollowEntity entity = FollowEntityMapper.fromFollow(follow); // 도메인 객체를 엔티티로 변환
        jpaFollowRepository.delete(entity); // 엔티티 삭제
    }
}
