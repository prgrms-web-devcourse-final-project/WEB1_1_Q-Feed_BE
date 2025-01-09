package com.wsws.moduledomain.usercontext.user.aggregate;

import com.wsws.moduledomain.authcontext.exception.PasswordMismatchException;
import com.wsws.moduledomain.usercontext.user.encoder.PasswordEncoder;
import com.wsws.moduledomain.usercontext.user.exception.AlreadyInactiveUserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void create_메서드를_통해_User를_생성할_수_있다(){


        //given
        String email = "tester1@gmail.com";
        String rawPassword = "Tester123!";
        String nickname = "testUser1";
        String profileImage = null;
        String description = "Hi! I am TESTUSER1";

        //when
        when(passwordEncoder.encode(rawPassword)).thenReturn("encodedPassword");

        User user = User.create(email, rawPassword, nickname, profileImage, description, passwordEncoder);

        //then
        assertThat(user.getId()).isNotNull();
        assertThat(user.getEmail().getValue()).isEqualTo(email);
        assertThat(user.getPassword().getValue()).isEqualTo("encodedPassword");
        assertThat(user.getNickname().getValue()).isEqualTo(nickname);
        assertThat(user.getProfileImage()).isEqualTo("");
        assertThat(user.getDescription()).isEqualTo(description);
        assertThat(user.getIsUsable()).isTrue();
    }

    @Test
    void updateProfile_메서드를_통해_프로필을_수정할_수_있다(){
        //given
        String email = "tester1@gmail.com";
        String rawPassword = "Tester123!";
        String nickname = "testUser1";
        String profileImage = null;
        String description = "Hi! I am TESTUSER1";
        when(passwordEncoder.encode(rawPassword)).thenReturn("encodedPassword");

        User user = User.create(email, rawPassword, nickname, profileImage, description, passwordEncoder);

        String newNickname = "testUser2";
        String newDescription = "Bye";

        //when
        user.updateProfile(newNickname, null, null);

        //then
        assertThat(user.getNickname().getValue()).isEqualTo(newNickname);
        assertThat(user.getProfileImage()).isEqualTo("");
        assertThat(user.getDescription()).isEqualTo(description);
    }

    @Test
    void changePassword_메서드를_통해_비밀번호를_변경할_수_있다(){
        //given

        String email = "tester1@gmail.com";
        String rawPassword = "Tester123!";
        String nickname = "testUser1";
        String profileImage = null;
        String description = "Hi! I am TESTUSER1";
        when(passwordEncoder.encode(rawPassword)).thenReturn("encodedPassword");

        User user = User.create(email, rawPassword, nickname, profileImage, description, passwordEncoder);

        String currentPassword = "Tester123!";
        String newPassword = "Tester123@";

        when(passwordEncoder.matches(currentPassword, "encodedPassword")).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("newEncodedPassword");


        //when
        user.changePassword(currentPassword, newPassword, passwordEncoder);


        //then

        assertThat(user.getPassword().getValue()).isEqualTo("newEncodedPassword");
    }

    @Test
    void changePassword_메서드에서_현재_비밀번호가_일치하지_않으면_예외를_던진다() {
        // given
        String email = "tester1@gmail.com";
        String rawPassword = "Tester123!";
        when(passwordEncoder.encode(rawPassword)).thenReturn("encodedPassword");

        User user = User.create(email, rawPassword, "testUser1", null, "Hi! I am TESTUSER1", passwordEncoder);

        String currentPassword = "WrongPassword";
        String newPassword = "Tester123@";

        when(passwordEncoder.matches(currentPassword, "encodedPassword")).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> user.changePassword(currentPassword, newPassword, passwordEncoder))
                .isInstanceOf(PasswordMismatchException.class);
    }

    @Test
    void resetPassword_메서드를_통해_비밀번호를_초기화할_수_있다() {
        // given
        String email = "tester1@gmail.com";
        String rawPassword = "Tester123!";
        when(passwordEncoder.encode(rawPassword)).thenReturn("encodedPassword");

        User user = User.create(email, rawPassword, "testUser1", null, "Hi! I am TESTUSER1", passwordEncoder);

        String newPassword = "Reset123@";
        when(passwordEncoder.encode(newPassword)).thenReturn("resetEncodedPassword");

        // when
        user.resetPassword(newPassword, passwordEncoder);

        // then
        assertThat(user.getPassword().getValue()).isEqualTo("resetEncodedPassword");
    }

    @Test
    void deactivate_메서드를_통해_사용자를_비활성화할_수_있다() {
        // given
        String email = "tester1@gmail.com";
        String rawPassword = "Tester123!";
        when(passwordEncoder.encode(rawPassword)).thenReturn("encodedPassword");

        User user = User.create(email, rawPassword, "testUser1", null, "Hi! I am TESTUSER1", passwordEncoder);

        // when
        user.deactivate();

        // then
        assertThat(user.getIsUsable()).isFalse();
    }

    @Test
    void 이미_비활성화된_사용자를_비활성화하려고_하면_예외를_던진다() {
        // given
        String email = "tester1@gmail.com";
        String rawPassword = "Tester123!";
        when(passwordEncoder.encode(rawPassword)).thenReturn("encodedPassword");

        User user = User.create(email, rawPassword, "testUser1", null, "Hi! I am TESTUSER1", passwordEncoder);

        user.deactivate();

        // when & then
        assertThatThrownBy(user::deactivate).isInstanceOf(AlreadyInactiveUserException.class);
    }

    @Test
    void activate_메서드를_통해_사용자를_활성화할_수_있다() {
        // given
        String email = "tester1@gmail.com";
        String rawPassword = "Tester123!";
        when(passwordEncoder.encode(rawPassword)).thenReturn("encodedPassword");

        User user = User.create(email, rawPassword, "testUser1", null, "Hi! I am TESTUSER1", passwordEncoder);

        user.deactivate();

        // when
        user.activate();

        // then
        assertThat(user.getIsUsable()).isTrue();
    }
    @Test
    void createSocialLoginUser_메서드를_통해_소셜_로그인_사용자를_생성할_수_있다() {
        // given
        String email = "socialuser@gmail.com";
        String nickname = "socialUser";
        String profileImageUrl = "https://example.com/image.jpg";

        // when
        User user = User.createSocialLoginUser(email, nickname, profileImageUrl);

        // then
        assertThat(user.getId()).isNotNull();
        assertThat(user.getEmail().getValue()).isEqualTo(email);
        assertThat(user.getPassword()).isNull(); // 소셜 로그인 사용자는 비밀번호가 null
        assertThat(user.getNickname().getValue()).isEqualTo(nickname);
        assertThat(user.getProfileImage()).isEqualTo(profileImageUrl);
        assertThat(user.getDescription()).isEqualTo("");
        assertThat(user.getIsUsable()).isTrue();
    }
}
