package com.wsws.moduleinfra.repo.user;

import com.wsws.moduledomain.user.Like;
import com.wsws.moduledomain.user.User;
import com.wsws.moduledomain.user.repo.LikeRepository;
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
    public Like save(Like like, User user) {
        UserEntity userEntity = jpaUserRepository.findById(user.getId().getValue())
                .orElseThrow(RuntimeException::new);

        LikeEntity likeEntity = LikeEntityMapper.toEntity(like);
        likeEntity.setUserEntity(userEntity);

        return LikeEntityMapper.toDomain(jpaLikeUserRepository.save(likeEntity));
    }

    @Override
    public boolean existsByTargetIdAndUserEntity(Long targetId, User user) {
        return jpaLikeUserRepository.existsByTargetIdAndUserEntity(targetId, UserEntityMapper.fromDomain(user));
    }
}
