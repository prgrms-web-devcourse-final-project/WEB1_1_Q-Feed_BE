package com.wsws.moduledomain.user.repo;

import com.wsws.moduledomain.user.User;
import com.wsws.moduledomain.user.vo.Email;
import com.wsws.moduledomain.user.vo.Nickname;
import com.wsws.moduledomain.user.vo.UserId;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByNickname(Nickname nickname);
    Optional<User> findByEmail(Email email);
    Optional<User> findById(UserId userId);
    User save(User user);
    void delete(User user);
    Boolean existsByEmail(Email email);
    Boolean existsByNickname(Nickname nickname);
}
