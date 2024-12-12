package com.wsws.moduleinfra.usercontext.user.repo;

import com.wsws.moduleinfra.usercontext.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByNickname(String nickname);
    boolean existsByEmail(String email);
    Boolean existsByNickname(String value);
}