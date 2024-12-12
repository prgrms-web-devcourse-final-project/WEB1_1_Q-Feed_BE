package com.wsws.moduleinfra.usercontext.user;

import com.wsws.moduledomain.usercontext.user.aggregate.User;
import com.wsws.moduledomain.usercontext.user.repo.UserRepository;
import com.wsws.moduledomain.usercontext.user.vo.Email;
import com.wsws.moduledomain.usercontext.user.vo.Nickname;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import com.wsws.moduleinfra.usercontext.entity.UserEntity;
import com.wsws.moduleinfra.usercontext.entity.UserEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

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
}
