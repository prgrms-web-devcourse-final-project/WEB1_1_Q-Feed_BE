package com.wsws.moduleinfra.usercontext.user.repo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wsws.moduledomain.usercontext.user.aggregate.User;
import com.wsws.moduledomain.usercontext.user.repo.UserRepository;
import com.wsws.moduledomain.usercontext.user.vo.Email;
import com.wsws.moduledomain.usercontext.user.vo.Nickname;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import com.wsws.moduleinfra.usercontext.user.entity.UserEntity;
import com.wsws.moduleinfra.usercontext.user.entity.UserEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.wsws.moduleinfra.usercontext.user.entity.QUserEntity.userEntity;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JPAQueryFactory queryFactory;
    private final JpaUserRepository jpaUserRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaUserRepository.findByEmail(email.getValue())
                .map(UserEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findByNickname(Nickname nickname) {
        return jpaUserRepository.findByNickname(nickname.getValue())
                .map(UserEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findById(UserId userId) {
        return jpaUserRepository.findById(userId.getValue())
                .map(UserEntityMapper::toDomain);
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserEntityMapper.fromDomain(user);
        return UserEntityMapper.toDomain(jpaUserRepository.save(entity));
    }

    @Override
    public void delete(User user) {
        UserEntity entity = UserEntityMapper.fromDomain(user);
        jpaUserRepository.delete(entity);
    }

    @Override
    public Boolean existsByEmail(Email email) {
        return jpaUserRepository.existsByEmail(email.getValue());
    }

    @Override
    public Boolean existsByNickname(Nickname nickname) {
        return jpaUserRepository.existsByNickname(nickname.getValue());
    }

    @Override
    public List<User> findUsersByIds(List<String> userIds) {
        // UserEntity 조회
        List<UserEntity> userEntities = queryFactory
                .selectFrom(userEntity)
                .where(userEntity.id.in(userIds))
                .fetch();

        // UserEntity를 도메인 객체로 변환
        return userEntities.stream()
                .map(UserEntityMapper::toDomain)
                .toList();
    }
}
