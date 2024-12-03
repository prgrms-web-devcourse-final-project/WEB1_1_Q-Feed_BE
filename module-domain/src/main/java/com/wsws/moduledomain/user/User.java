package com.wsws.moduledomain.user;

import com.wsws.moduledomain.auth.exception.PasswordMismatchException;
import com.wsws.moduledomain.category.vo.CategoryId;
import com.wsws.moduledomain.user.vo.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
public class User {
    private UserId id;

    private Email email;

    private Password password;

    private Nickname nickname;

    private String profileImage;

    private Boolean isUsable = true;

    private String description;

    private UserRole userRole;

    private List<UserInterest> interests = new ArrayList<>();



    // 사용자 생성
    public static User create(String email, String rawPassword, String nickname, String profileImage, PasswordEncoder passwordEncoder) {



        User user = new User();
        user.id = UserId.create(); // 새로운 UserId 생성
        user.email = Email.from(email); // Email VO 생성 및 유효성 검증
        user.password = Password.encode(rawPassword, passwordEncoder); // Password VO 생성 및 암호화
        user.nickname = Nickname.from(nickname); // Nickname VO 생성 및 유효성 검증
        user.profileImage = profileImage != null ? profileImage : ""; // 프로필 이미지가 없으면 기본값 설정
        user.isUsable = true; // 기본 활성 상태
        user.userRole = UserRole.ROLE_USER; // 기본 역할
        return user;
    }

    public static User transform(String userid,String email,String encodedPassword, String nickname, String profileImage){
        User user = new User();
        user.id = UserId.of(userid);
        user.email = Email.from(email);
        user.password = Password.of(encodedPassword);
        user.nickname = Nickname.from(nickname);
        user.profileImage = profileImage;

        return user;
    }

    //사용자 관심사 추가
    public void addInterest(CategoryId categoryId) {
        UserInterest interest = UserInterest.create(categoryId);
        if (!this.interests.contains(interest)) {
            this.interests.add(interest);
        }
    }

    // 관심사 제거
    public void removeInterest(CategoryId categoryId) {
        this.interests.removeIf(interest -> interest.getCategoryId().equals(categoryId));
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

    public void resetPassword(String newPassword, PasswordEncoder passwordEncoder) {

        Password.validate(newPassword);

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
