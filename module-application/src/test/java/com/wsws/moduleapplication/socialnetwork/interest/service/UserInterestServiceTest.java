package com.wsws.moduleapplication.socialnetwork.interest.service;

import com.wsws.moduledomain.category.Category;
import com.wsws.moduledomain.category.repo.CategoryRepository;
import com.wsws.moduledomain.category.vo.CategoryName;
import com.wsws.moduledomain.socialnetwork.interest.UserInterest;
import com.wsws.moduledomain.socialnetwork.interest.UserInterestRepository;
import com.wsws.moduledomain.usercontext.user.aggregate.User;
import com.wsws.moduledomain.usercontext.user.repo.UserRepository;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserInterestServiceTest {

    @Mock
    private UserInterestRepository userInterestRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserInterestService userInterestService;

    @Nested
    @DisplayName("createInterests 메서드는")
    class DescribeCreateInterests {

        @Test
        @DisplayName("userId와 관심사 리스트를 리스트를 받아서 사용자 관심사를 설정할 수 있다.")
        void itCreateInterestsTest() {
            //given
            String userId = "user1";
            List<String> interestCategoryNames = List.of("sports","fashion");
            User user = mock(User.class);

            when(userRepository.findById(UserId.of(userId))).thenReturn(Optional.of(user));
            when(categoryRepository.findByCategoryName(any())).thenReturn(mock(Category.class));

            //when
            userInterestService.createInterests(userId, interestCategoryNames);

            //then
            verify(userInterestRepository, times(1)).save(any(List.class));


        }
    }
    @Nested
    @DisplayName("updateInterests 메서드는")
    class DescribeUpdateInterests {

        @Test
        @DisplayName("userId와 새로운 관심사 리스트를 받아서 기존 관심사를 삭제하고 새로운 관심사를 저장할 수 있다")
        void itUpdatesInterests() {
            // given
            String userId = "user1";
            List<String> newInterestCategoryNames = List.of("travel", "culture");
            User user = mock(User.class);

            when(userRepository.findById(UserId.of(userId))).thenReturn(Optional.of(user));
            when(categoryRepository.findByCategoryName(any())).thenReturn(mock(Category.class));

            // when
            userInterestService.updateInterests(userId, newInterestCategoryNames);

            // then
            verify(userInterestRepository, times(1)).deleteByUserId(user.getId());
            verify(userInterestRepository, times(1)).save(any(List.class));
        }
    }

    @Nested
    @DisplayName("getUserInterests 메서드는")
    class DescribeGetUserInterests {

        @Test
        @DisplayName("userId를 받아 사용자의 관심사 목록을 반환할 수 있다")
        void itReturnsUserInterests() {
            // given
            String userId = "user1";
            User user = mock(User.class);
            when(userRepository.findById(UserId.of(userId))).thenReturn(Optional.of(user));

            List<UserInterest> mockUserInterests = List.of(mock(UserInterest.class), mock(UserInterest.class));
            when(userInterestRepository.findByUserId(any())).thenReturn(mockUserInterests);

            Category mockCategory = mock(Category.class);
            when(categoryRepository.findById(any())).thenReturn(mockCategory);
            when(mockCategory.getCategoryName()).thenReturn(CategoryName.SPORTS);

            // when
            List<String> interests = userInterestService.getUserInterests(userId);

            // then
            assertThat(interests).isNotEmpty();
            assertThat(interests).contains("sports");

            verify(userRepository, times(1)).findById(UserId.of(userId));
            verify(userInterestRepository, times(1)).findByUserId(any());
            verify(categoryRepository, times(mockUserInterests.size())).findById(any());
        }
    }

}