package com.wsws.moduleinfra.repo.user;

import com.wsws.moduledomain.user.Like;
import com.wsws.moduledomain.user.User;
import com.wsws.moduledomain.user.repo.LikeRepository;
import com.wsws.moduledomain.user.vo.TargetType;
import com.wsws.moduleinfra.entity.user.LikeEntity;
import com.wsws.moduleinfra.entity.user.LikeEntityMapper;
import com.wsws.moduleinfra.entity.user.UserEntity;
import com.wsws.moduleinfra.entity.user.UserEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
