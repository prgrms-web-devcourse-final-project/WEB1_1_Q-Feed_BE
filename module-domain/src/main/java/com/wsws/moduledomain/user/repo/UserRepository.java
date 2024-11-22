package com.wsws.moduledomain.user.repo;

import com.wsws.moduledomain.user.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByNickname(String nickname);
    Optional<User> findByEmail(String email);
    void save(User user);
}
