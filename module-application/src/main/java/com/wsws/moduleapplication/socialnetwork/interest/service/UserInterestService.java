package com.wsws.moduleapplication.socialnetwork.interest.service;

import com.wsws.moduleapplication.usercontext.user.exception.UserNotFoundException;
import com.wsws.moduledomain.category.Category;
import com.wsws.moduledomain.category.repo.CategoryRepository;
import com.wsws.moduledomain.category.vo.CategoryId;
import com.wsws.moduledomain.category.vo.CategoryName;
import com.wsws.moduledomain.socialnetwork.interest.UserInterest;
import com.wsws.moduledomain.socialnetwork.interest.UserInterestRepository;
import com.wsws.moduledomain.usercontext.user.aggregate.User;
import com.wsws.moduledomain.usercontext.user.repo.UserRepository;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserInterestService {
    private final UserInterestRepository userInterestRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

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
                .map(categoryId -> UserInterest.create(categoryId,user.getId()))
                .toList();

        // 관심사 저장
        userInterestRepository.save(userInterests);
    }

    public void updateInterests(String userId, List<String> interestCategoryNames) {
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
                .map(categoryId -> UserInterest.create(categoryId,user.getId()))
                .toList();

        // 기존 관심사 삭제
        userInterestRepository.deleteByUserId(user.getId());

        // 새로운 관심사 저장
        userInterestRepository.save(userInterests);
    }

    @Transactional(readOnly = true)
    public List<String> getUserInterests(String userId) {
        // 사용자 확인
        User user = findUserByIdOrThrow(userId);

        // 관심사 조회
        List<UserInterest> userInterests = userInterestRepository.findByUserId(user.getId());

        // 관심사를 CategoryName(String) 리스트로 변환
        return userInterests.stream()
                .map(userInterest -> {
                    CategoryId categoryId = userInterest.getCategoryId();
                    Category category = categoryRepository.findById(categoryId);
                    return category.getCategoryName().getName(); // CategoryName의 문자열 반환
                })
                .toList();
    }

    // 사용자 조회
    private User findUserByIdOrThrow(String userId) {
        return userRepository.findById(UserId.of(userId))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
    }
}
