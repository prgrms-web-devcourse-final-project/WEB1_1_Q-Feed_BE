package com.wsws.moduleapplication.user.service;

import com.wsws.moduleapplication.user.dto.PasswordChangeServiceDto;
import com.wsws.moduleapplication.user.dto.RegisterUserRequest;
import com.wsws.moduleapplication.user.dto.UpdateProfileServiceDto;
import com.wsws.moduleapplication.util.ProfileImageValidator;
import com.wsws.modulecommon.service.FileStorageService;
import com.wsws.moduledomain.category.Category;
import com.wsws.moduledomain.category.repo.CategoryRepository;
import com.wsws.moduledomain.category.vo.CategoryId;
import com.wsws.moduledomain.category.vo.CategoryName;
import com.wsws.moduledomain.user.PasswordEncoder;
import com.wsws.moduledomain.user.User;
import com.wsws.moduleapplication.user.exception.DuplicateEmailException;
import com.wsws.moduleapplication.user.exception.DuplicateNicknameException;
import com.wsws.moduleapplication.user.exception.ProfileImageProcessingException;
import com.wsws.moduleapplication.user.exception.UserNotFoundException;
import com.wsws.moduledomain.user.repo.UserInterestRepository;
import com.wsws.moduledomain.user.repo.UserRepository;
import com.wsws.moduledomain.user.vo.Email;
import com.wsws.moduledomain.user.vo.Nickname;
import com.wsws.moduledomain.user.vo.UserId;
import com.wsws.moduledomain.user.vo.UserInterest;
import com.wsws.moduleinfra.repo.category.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final FileStorageService fileStorageService;
    private final UserInterestRepository userInterestRepository;



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
        System.out.println("3resquest password:" + request.password());
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

    public void createInterests(String userId, List<String> interestCategoryNames) {
        // 사용자 조회
        User user = findUserByIdOrThrow(userId);

        // CategoryName → CategoryId
        List<CategoryId> categoryIds = interestCategoryNames.stream()
                .map(CategoryName::findByName) // String → CategoryName (ENUM)
                .map(categoryRepository::findByCategoryName) // CategoryName → Category
                .map(Category::getId) // Category → CategoryId
                .toList();

        // UserInterest 도메인 리스트 생성
        List<UserInterest> userInterests = categoryIds.stream()
                .map(UserInterest::create)
                .toList();

        // 관심사 저장
        userInterestRepository.save(user.getId(), userInterests);
    }

    public void updateInterests(String userId, List<String> interestCategoryNames) {
        // 사용자 조회
        User user = findUserByIdOrThrow(userId);

        // CategoryName → CategoryId
        List<CategoryId> categoryIds = interestCategoryNames.stream()
                .map(CategoryName::valueOf) // String → CategoryName (ENUM)
                .map(categoryRepository::findByCategoryName) // CategoryName → Category
                .map(Category::getId) // Category → CategoryId
                .toList();

        // UserInterest 도메인 리스트 생성
        List<UserInterest> userInterests = categoryIds.stream()
                .map(UserInterest::create)
                .toList();

        // 기존 관심사 삭제
        userInterestRepository.deleteByUserId(user.getId());

        // 새로운 관심사 저장
        userInterestRepository.save(user.getId(), userInterests);
    }




    // Unique 검사

    private void validateUniqueEmail(Email email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw DuplicateEmailException.EXCEPTION;
        }
    }

    private void validateUniqueNickname(Nickname nickname) {
        if (userRepository.findByNickname(nickname).isPresent()) {
            throw DuplicateNicknameException.EXCEPTION;
        }
    }

    // 사용자 조회
    private User findUserByIdOrThrow(String userId) {
        return userRepository.findById(UserId.of(userId))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
    }

    //프로필 이미지 처리
    private String processProfileImage(MultipartFile profileImageFile) {
        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            try {
                ProfileImageValidator.validate(profileImageFile);
                return fileStorageService.saveFile(profileImageFile);
            } catch (Exception e) {
                throw ProfileImageProcessingException.EXCEPTION;
            }
        }
        return null; // 이미지가 없는 경우
    }


}
