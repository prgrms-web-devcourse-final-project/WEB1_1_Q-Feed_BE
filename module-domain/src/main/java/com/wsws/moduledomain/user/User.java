package com.wsws.moduledomain.user;

import com.wsws.moduledomain.user.vo.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    private Nickname nickname;

    private String profileImage;

    private Boolean isUsable = true;

    @Column
    private String description;


    @Enumerated(value = EnumType.STRING)
    private UserRole userRole;

    public User(String email, String rawPassword, String nickname, String profileImage, PasswordEncoder passwordEncoder) {
        this.id = new UserId().create(); // UserId를 새로 생성
        this.email = new Email(email); // Email VO 생성
        this.password = Password.encode(rawPassword, passwordEncoder); // Password VO를 암호화 후 생성
        this.nickname = new Nickname(nickname); // Nickname VO 생성
        this.profileImage = (profileImage != null) ? profileImage : ""; // 없으면 빈 string
        this.isUsable = true;
        this.userRole = UserRole.findByRole("user"); // 기본 역할
    }

    // 프로필 업데이트
    public void updateProfile(String nickname, String profileImage, String description){
        if(nickname != null){
            this.nickname = new Nickname(nickname);
        }
        if(profileImage != null){
            this.profileImage = profileImage;
        }
        if(description != null){
            this.description = description;
        }
    }

    // 비밀번호 변경
    public void changePassword(String currentPassword, String newPassword, PasswordEncoder passwordEncoder){
        if(!this.password.matches(currentPassword, passwordEncoder)){//현재 패스워드가 올바르게 입력되지않았으면
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다");
        }
        this.password = Password.encode(newPassword, passwordEncoder);
    }
}
