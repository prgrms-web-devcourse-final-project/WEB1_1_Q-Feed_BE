package com.wsws.moduleinfra.entity.user;


import com.wsws.moduledomain.user.User;
import com.wsws.moduledomain.user.vo.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private String id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "is_usable", nullable = false)
    private Boolean isUsable;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole userRole;



    // 엔티티 생성자 (도메인 데이터를 엔티티로 변환)
    public UserEntity(String id, String email, String password, String nickname,
                      String profileImage, Boolean isUsable, String description, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage != null ? profileImage : "";
        this.isUsable = isUsable != null ? isUsable : true;
        this.description = description;
        this.userRole = userRole != null ? userRole : UserRole.ROLE_USER;
    }

}