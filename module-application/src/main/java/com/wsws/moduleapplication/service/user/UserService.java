package com.wsws.moduleapplication.service.user;

import com.wsws.moduleapplication.dto.user.*;
import com.wsws.moduleapplication.util.ProfileImageValidator;
import com.wsws.modulecommon.service.FileStorageService;
import com.wsws.moduledomain.user.PasswordEncoder;
import com.wsws.moduledomain.user.User;
import com.wsws.moduledomain.user.exception.DuplicateEmailException;
import com.wsws.moduledomain.user.exception.UserNotFoundException;
import com.wsws.moduledomain.user.repo.UserRepository;
import com.wsws.moduledomain.user.vo.Email;
import com.wsws.moduledomain.user.vo.Nickname;
import com.wsws.moduledomain.user.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;



    // 회원가입
    public void register(RegisterUserRequest request) {
        validateUniqueEmail(Email.from(request.email()));
        validateUniqueNickname(Nickname.from(request.nickname()));

        // 프로필 이미지 처리
        String profileImageUrl = processProfileImage(request.profileImageFile());

        // 사용자 생성
        User user = User.create(
                request.email(),
                request.password(),
                request.nickname(),
                profileImageUrl,
                passwordEncoder
        );

        userRepository.save(user);
    }


    // 사용자 프로필 수정
    public void updateProfile(UpdateProfileServiceDto dto, String userId) {
        User user = findUserByIdOrThrow(userId);

        if (!user.getNickname().getValue().equals(dto.nickname())) {
            validateUniqueNickname(Nickname.from(dto.nickname()));
        }

        // 프로필 이미지 처리
        String profileImageUrl = processProfileImage(dto.profileImageFile());
        if (profileImageUrl == null) {
            profileImageUrl = user.getProfileImage(); // 기존 이미지 유지
        }

        user.updateProfile(dto.nickname(), profileImageUrl, dto.description());
    }

    // 비밀번호 변경
    public void changePassword(PasswordChangeServiceDto dto, String userId) {
        User user = findUserByIdOrThrow(userId);
        user.changePassword(dto.currentPassword(), dto.newPassword(), passwordEncoder);
    }

    //사용자 탈퇴
    public void deleteUser(String userId) {
        User user = findUserByIdOrThrow(userId);
        userRepository.delete(user);
    }




    // Unique 검사

    private void validateUniqueEmail(Email email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateEmailException("이미 사용 중인 이메일입니다.");
        }
    }

    private void validateUniqueNickname(Nickname nickname) {
        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }
    }

    // 사용자 조회
    private User findUserByIdOrThrow(String userId) {
        return userRepository.findById(UserId.of(userId))
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
    }

    //프로필 이미지 처리
    private String processProfileImage(MultipartFile profileImageFile) {
        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            try {
                ProfileImageValidator.validate(profileImageFile);
                return fileStorageService.saveFile(profileImageFile);
            } catch (Exception e) {
                throw new IllegalStateException("프로필 이미지 처리 중 오류 발생.", e);
            }
        }
        return null; // 이미지가 없는 경우
    }


}
