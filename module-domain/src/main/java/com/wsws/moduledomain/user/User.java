package com.wsws.moduledomain.user;

import com.wsws.moduledomain.auth.exception.PasswordMismatchException;
import com.wsws.moduledomain.user.vo.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User {
    @EmbeddedId
    private UserId id;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    @Embedded
    @Column(unique = true)
    private Nickname nickname;

    private String profileImage;

    private Boolean isUsable = true;

    @Column
    private String description;

    @Enumerated(value = EnumType.STRING)
    private UserRole userRole;

    // 사용자 생성
    public static User create(String email, String rawPassword, String nickname, String profileImage, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.id = UserId.create(); // UserId를 새로 생성
        user.email = Email.from(email); // Email VO 생성
        user.password = Password.encode(rawPassword, passwordEncoder); // Password VO를 암호화 후 생성
        user.nickname = Nickname.from(nickname); // Nickname VO 생성
        user.profileImage = (profileImage != null) ? profileImage : ""; // 프로필 이미지가 없으면 빈 문자열
        user.isUsable = true;
        user.userRole = UserRole.findByRole("user"); // 기본 역할
        return user;
    }

    // 프로필 업데이트
    public void updateProfile(String nickname, String profileImage, String description) {
        if (nickname != null) {
            this.nickname = Nickname.from(nickname);
        }
        if (profileImage != null) {
            this.profileImage = profileImage;
        }
        if (description != null) {
            this.description = description;
        }
    }

    // 비밀번호 변경
    public void changePassword(String currentPassword, String newPassword, PasswordEncoder passwordEncoder) {
        // 현재 비밀번호 확인
        this.password.matches(currentPassword, passwordEncoder);
        // 새 비밀번호 유효성 검증
        Password.validate(newPassword);
        // 새 비밀번호 설정
        this.password = Password.encode(newPassword, passwordEncoder);
    }

    // 사용자 상태 변경
    public void deactivate() {
        this.isUsable = false;
    }

    public void activate() {
        this.isUsable = true;
    }
}
