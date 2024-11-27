package com.wsws.moduleinfra.repo.user;

import com.wsws.moduledomain.user.User;
import com.wsws.moduledomain.user.repo.UserRepository;
import com.wsws.moduledomain.user.vo.Email;
import com.wsws.moduledomain.user.vo.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<User, UserId>, UserRepository {

    Optional<User> findByEmail(String email);

    // 도메인 인터페이스 메서드 구현
    @Override
    default Optional<User> findByEmail(Email email) {
        return findByEmail(email.getValue());
    }



}
