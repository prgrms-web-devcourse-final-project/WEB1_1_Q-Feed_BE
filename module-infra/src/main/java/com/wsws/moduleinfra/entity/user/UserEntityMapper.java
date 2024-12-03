package com.wsws.moduleinfra.entity.user;

import com.wsws.moduledomain.user.User;

public class UserEntityMapper {

    public static User toDomain(UserEntity entity) {
        return User.transform(
                entity.getId(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getNickname(),
                entity.getProfileImage()
        );
    }

    // User → UserEntity (Domain to Entity)
    public static UserEntity fromDomain(User user) {
        return new UserEntity(
                user.getId().getValue(),
                user.getEmail().getValue(),
                user.getPassword().getValue(),
                user.getNickname().getValue(),
                user.getProfileImage(),
                user.getIsUsable(),
                user.getDescription(),
                user.getUserRole()
        );
    }
}
