package com.wsws.moduleinfra.usercontext.entity;

import com.wsws.moduledomain.usercontext.user.aggregate.User;

public class UserEntityMapper {

    public static User toDomain(UserEntity entity) {
        return User.transform(
                entity.getId(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getNickname(),
                entity.getProfileImage(),
                entity.getDescription()
        );
    }

    // User → UserEntity (Domain to Entity)
    public static UserEntity fromDomain(User user) {
        return new UserEntity(
                user.getId().getValue(),
                user.getEmail().getValue(),
                user.getPassword() != null ? user.getPassword().getValue() : null,
                user.getNickname().getValue(),
                user.getProfileImage(),
                user.getIsUsable(),
                user.getDescription(),
                user.getUserRole()
        );
    }
}
