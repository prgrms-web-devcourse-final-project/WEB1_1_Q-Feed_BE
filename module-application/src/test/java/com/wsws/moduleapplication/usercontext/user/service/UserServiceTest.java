package com.wsws.moduleapplication.usercontext.user.service;

import com.wsws.moduleapplication.socialnetwork.interest.service.UserInterestService;
import com.wsws.moduleapplication.usercontext.user.dto.PasswordChangeServiceDto;
import com.wsws.moduleapplication.usercontext.user.dto.RegisterUserRequest;
import com.wsws.moduleapplication.usercontext.user.dto.UpdateProfileServiceDto;
import com.wsws.moduleapplication.usercontext.user.event.UserDeletedEvent;
import com.wsws.moduleapplication.usercontext.user.event.UserUpdatedEvent;
import com.wsws.moduleapplication.usercontext.user.exception.DuplicateEmailException;
import com.wsws.moduleapplication.usercontext.user.exception.DuplicateNicknameException;
import com.wsws.moduleapplication.usercontext.user.exception.UserNotFoundException;
import com.wsws.modulecommon.service.FileStorageService;
import com.wsws.moduledomain.usercontext.user.aggregate.User;
import com.wsws.moduledomain.usercontext.user.encoder.PasswordEncoder;
import com.wsws.moduledomain.usercontext.user.repo.UserRepository;
import com.wsws.moduledomain.usercontext.user.vo.Email;
import com.wsws.moduledomain.usercontext.user.vo.Nickname;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserInterestService userInterestService;
    @Mock
    private FileStorageService fileStorageService;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private UserService userService;

    private static final String EMAIL = "tester1@gmail.com";
    private static final String PASSWORD = "Tester123!";
    private static final String NICKNAME = "testUser1";
    private static final String DESCRIPTION = "Hi! I am TESTUSER1";

    @Nested
    @DisplayName("register 메서드는")
    class DescribeRegister {

        @Test
        @DisplayName("새 User를 생성하고 저장한 뒤 관심사도 생성할 수 있습니다.")
        void itCreatesNewUserWhenEmailAndNicknameAreUnique() {
            // given
            RegisterUserRequest request = RegisterUserRequest.builder()
                    .email(EMAIL)
                    .nickname(NICKNAME)
                    .password(PASSWORD)
                    .profileImageFile(null)
                    .description(DESCRIPTION)
                    .interestCategoryNames(List.of("sports", "fashion"))
                    .build();

            when(userRepository.findByEmail(Email.from(EMAIL))).thenReturn(Optional.empty()); //이메일 검증
            when(userRepository.findByNickname(Nickname.from(NICKNAME))).thenReturn(Optional.empty());
            when(passwordEncoder.encode(PASSWORD)).thenReturn("encodedPassword");
            doAnswer(invocation -> invocation.getArgument(0))
                    .when(userRepository).save(any(User.class));

            // when
            userService.register(request);

            // then
            var userCaptor = forClass(User.class);
            verify(userRepository, times(1)).save(userCaptor.capture());
            User savedUser = userCaptor.getValue();

            assertThat(savedUser.getEmail().getValue()).isEqualTo(EMAIL);
            assertThat(savedUser.getNickname().getValue()).isEqualTo(NICKNAME);
            assertThat(savedUser.getPassword().getValue()).isEqualTo("encodedPassword");
            assertThat(savedUser.getDescription()).isEqualTo(DESCRIPTION);

            verify(userInterestService, times(1))
                    .createInterests(eq(savedUser.getId().getValue()), eq(request.interestCategoryNames()));
        }

        @Test
        @DisplayName("이미 존재하는 이메일이면 DuplicateEmailException 예외를 던진다.")
        void itThrowsDuplicateEmailExceptionIfEmailExists() {
            // given
            RegisterUserRequest request = RegisterUserRequest.builder()
                    .email(EMAIL)
                    .nickname(NICKNAME)
                    .password(PASSWORD)
                    .profileImageFile(null)
                    .description(DESCRIPTION)
                    .interestCategoryNames(List.of())
                    .build();

            when(userRepository.findByEmail(Email.from(EMAIL)))
                    .thenReturn(Optional.of(mock(User.class)));

            // when & then
            assertThatThrownBy(() -> userService.register(request))
                    .isInstanceOf(DuplicateEmailException.class);

            verify(userRepository, never()).save(any(User.class));
            verify(userInterestService, never()).createInterests(any(), any());
        }

        @Test
        @DisplayName("이미 존재하는 닉네임이면 DuplicateNicknameException 예외를 던진다.")
        void itThrowsDuplicateNicknameExceptionIfNicknameExists() {
            // given
            RegisterUserRequest request = RegisterUserRequest.builder()
                    .email(EMAIL)
                    .nickname(NICKNAME)
                    .password(PASSWORD)
                    .profileImageFile(null)
                    .description(DESCRIPTION)
                    .interestCategoryNames(List.of())
                    .build();

            when(userRepository.findByEmail(Email.from(EMAIL))).thenReturn(Optional.empty());
            when(userRepository.findByNickname(Nickname.from(NICKNAME)))
                    .thenReturn(Optional.of(mock(User.class)));

            // when & then
            assertThatThrownBy(() -> userService.register(request))
                    .isInstanceOf(DuplicateNicknameException.class);

            verify(userRepository, never()).save(any(User.class));
            verify(userInterestService, never()).createInterests(any(), any());
        }
    }

    @Nested
    @DisplayName("updateProfile 메서드는")
    class DescribeUpdateProfile {
        @Test
        @DisplayName("UpdateProfileServiceDto를 받아서 유저의 프로필을 변경할 수 있습니다.")
        void itUpdatesUserProfile() {
            // given
            String userId = "test-user-id";

            // MultipartFile Mock 설정
            MultipartFile mockFile = mock(MultipartFile.class);
            when(mockFile.isEmpty()).thenReturn(false);
            when(mockFile.getContentType()).thenReturn("image/jpeg");
            when(mockFile.getSize()).thenReturn(1024L);

            // UpdateProfileServiceDto 생성
            UpdateProfileServiceDto dto = new UpdateProfileServiceDto(
                    "newNickname",
                    "Updated description",
                    mockFile
            );

            // User Mock 설정
            User existingUser = mock(User.class);
            when(userRepository.findById(UserId.of(userId))).thenReturn(Optional.of(existingUser));
            when(userRepository.findByNickname(Nickname.from(dto.nickname()))).thenReturn(Optional.empty());
            when(existingUser.getNickname()).thenReturn(Nickname.from("oldNickname"));

            // FileStorageService Mock 설정
            when(fileStorageService.saveFile(mockFile)).thenReturn("updatedProfileImageUrl");

            // when
            userService.updateProfile(dto, userId);

            // then
            verify(existingUser, times(1)).updateProfile(
                    eq(dto.nickname()),
                    eq("updatedProfileImageUrl"),
                    eq(dto.description())
            );

            verify(userRepository, times(1)).save(existingUser);
            verify(eventPublisher, times(1)).publishEvent(any(UserUpdatedEvent.class));
        }

        @Test
        @DisplayName("프로필 이미지가 없으면 기존 이미지를 유지합니다")
        void itKeepsExistingProfileImageIfNoNewImageIsProvided() {
            // given
            String userId = "test-user-id";

            // UpdateProfileServiceDto 생성
            UpdateProfileServiceDto dto = new UpdateProfileServiceDto(
                    "newNickname",
                    "Updated description",
                    null // 프로필 이미지 없음
            );

            // User Mock 설정
            User existingUser = mock(User.class);
            when(userRepository.findById(UserId.of(userId))).thenReturn(Optional.of(existingUser));
            when(userRepository.findByNickname(Nickname.from(dto.nickname()))).thenReturn(Optional.empty());
            when(existingUser.getNickname()).thenReturn(Nickname.from("oldNickname"));
            when(existingUser.getProfileImage()).thenReturn("existingProfileImageUrl");

            // when
            userService.updateProfile(dto, userId);

            // then
            verify(existingUser, times(1)).updateProfile(
                    eq(dto.nickname()),
                    eq("existingProfileImageUrl"),
                    eq(dto.description())
            );

            verify(userRepository, times(1)).save(existingUser);
            verify(eventPublisher, times(1)).publishEvent(any(UserUpdatedEvent.class));
        }


    }

    @Nested
    @DisplayName("changePassword 메서드는")
    class DescribeChangePassword {

        @Test
        @DisplayName("PasswordChangeServiceDto를 받아서 유저의 비밀번호를 변경한다")
        void itChangePassword() {
            String userId = "test-user-id";

            String currentPassword = PASSWORD;
            String newPassword = "Tester123@";

            PasswordChangeServiceDto dto = new PasswordChangeServiceDto(currentPassword,newPassword);

            User existingUser = mock(User.class);
            when(userRepository.findById(UserId.of(userId))).thenReturn(Optional.of(existingUser));


            //when
            userService.changePassword(dto, userId);

            //then
            verify(existingUser, times(1)).changePassword(
                    eq(currentPassword),
                    eq(newPassword),
                    eq(passwordEncoder)
            );

            verify(userRepository, times(1)).save(existingUser);

        }
        @Test
        @DisplayName("존재하지 않는 사용자 ID로 호출되면 UserNotFoundException을 던진다")
        void itThrowsUserNotFoundExceptionIfUserDoesNotExist() {
            // given
            String userId = "non-existent-user-id";

            PasswordChangeServiceDto dto = new PasswordChangeServiceDto(PASSWORD, "Tester123@");

            when(userRepository.findById(UserId.of(userId))).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userService.changePassword(dto, userId))
                    .isInstanceOf(UserNotFoundException.class);

            verify(userRepository, never()).save(any(User.class));
        }



    }

    @Nested
    @DisplayName("deleteUser 메서드는")
    class DescribeDeleteUser {

        @Test
        @DisplayName("유효한 사용자 ID를 받아서 해당 사용자를 삭제하고 이벤트를 발행한다")
        void itDeletesUserAndPublishesEvent() {
            // given
            String userId = "test-user-id";

            User existingUser = mock(User.class);
            when(userRepository.findById(UserId.of(userId))).thenReturn(Optional.of(existingUser));

            // when
            userService.deleteUser(userId);

            // then
            verify(userRepository, times(1)).delete(existingUser); // 삭제 호출 확인
            verify(eventPublisher, times(1)).publishEvent(any(UserDeletedEvent.class)); // 이벤트 발행 확인
        }

        @Test
        @DisplayName("존재하지 않는 사용자 ID로 호출되면 UserNotFoundException을 던진다")
        void itThrowsUserNotFoundExceptionIfUserDoesNotExist() {
            // given
            String userId = "non-existent-user-id";

            when(userRepository.findById(UserId.of(userId))).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userService.deleteUser(userId))
                    .isInstanceOf(UserNotFoundException.class);

            verify(userRepository, never()).delete(any(User.class)); // 삭제 호출되지 않음
            verify(eventPublisher, never()).publishEvent(any(UserDeletedEvent.class)); // 이벤트 발행되지 않음
        }
    }





}



