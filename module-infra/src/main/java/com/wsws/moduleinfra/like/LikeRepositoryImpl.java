package com.wsws.moduleinfra.like;

import com.wsws.moduledomain.feed.like.Like;
import com.wsws.moduledomain.feed.like.LikeRepository;
import com.wsws.moduledomain.feed.like.TargetType;
import com.wsws.moduleinfra.entity.feed.LikeEntity;
import com.wsws.moduleinfra.entity.feed.LikeEntityMapper;
import com.wsws.moduleinfra.usercontext.entity.UserEntity;
import com.wsws.moduleinfra.usercontext.user.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {
    private final JpaLikeUserRepository jpaLikeUserRepository;
    private final JpaUserRepository jpaUserRepository;

    @Override
    public Like save(Like like) {
        UserEntity userEntity = jpaUserRepository.findById(like.getUserId().getValue())
                .orElse(null);

        LikeEntity likeEntity = LikeEntityMapper.toEntity(like);
        likeEntity.setUserEntity(userEntity);

        return LikeEntityMapper.toDomain(jpaLikeUserRepository.save(likeEntity));
    }

    @Override
    public boolean existsByTargetIdAndUserIdAndTargetType(Long targetId, String userId, TargetType targetType) {
        return jpaLikeUserRepository.existsByTargetIdAndUserIdAndTargetType(targetId, userId, targetType);
    }

    @Override
    public void deleteByTargetIdAndUserId(Long targetId, String userId) {
        jpaLikeUserRepository.deleteByTargetIdAndUserId(targetId, userId);
    }

    @Override
    public List<Like> findByUserId(String userId) {
        return jpaLikeUserRepository.findByUserId(userId).stream()
                .map(LikeEntityMapper::toDomain)
                .toList();
    }
}
