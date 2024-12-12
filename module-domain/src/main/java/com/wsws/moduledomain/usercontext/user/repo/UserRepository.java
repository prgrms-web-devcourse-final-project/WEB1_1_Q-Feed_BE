package com.wsws.moduledomain.usercontext.user.repo;

import com.wsws.moduledomain.usercontext.user.aggregate.User;
import com.wsws.moduledomain.usercontext.user.vo.Email;
import com.wsws.moduledomain.usercontext.user.vo.Nickname;
import com.wsws.moduledomain.usercontext.user.vo.UserId;

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
