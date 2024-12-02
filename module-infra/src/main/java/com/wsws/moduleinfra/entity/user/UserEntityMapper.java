package com.wsws.moduleinfra.entity.user;

import com.wsws.moduledomain.user.User;

public class UserEntityMapper {

    public static User toDomain(UserEntity entity) {
        return User.create(
                entity.getEmail(),
                null, // 원본 비밀번호는 알 수 없음
                entity.getNickname(),
                entity.getProfileImage(),
                null // PasswordEncoder는 외부에서 주입받아야 함
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
