package com.wsws.moduleapplication.socialnetwork.recommendation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.wsws.moduleapplication.socialnetwork.recommendation.dto.UserRecommendationResponse;
import com.wsws.moduledomain.cache.CacheManager;
import com.wsws.moduledomain.category.vo.CategoryId;
import com.wsws.moduledomain.socialnetwork.follow.repo.FollowReadRepository;
import com.wsws.moduledomain.socialnetwork.interest.UserInterest;
import com.wsws.moduledomain.socialnetwork.interest.UserInterestRepository;
import com.wsws.moduledomain.usercontext.user.aggregate.User;
import com.wsws.moduledomain.usercontext.user.repo.UserRepository;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import com.wsws.moduledomain.usercontext.user.vo.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private UserInterestRepository userInterestRepository;

    @Mock
    private FollowReadRepository followReadRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CacheManager cacheManager;

    @InjectMocks
    private RecommendationService recommendationService;

    @Nested
    @DisplayName("getRecommendations 메서드는")
    class DescribeGetRecommendations {

        @Test
        @DisplayName("캐시에 추천 데이터가 존재하면 이를 반환한다.")
        void itReturnsRecommendationsFromCacheIfExists() {
            //given
            String userId = "user123";
            String cacheKey = "recommendation:" + userId;
            List<UserRecommendationResponse> cachedRecommendations = List.of(
                    new UserRecommendationResponse("user1", "nickname1", "profile1", 10L)
            );

            when(cacheManager.getJson(eq(cacheKey), any(TypeReference.class))).thenReturn(cachedRecommendations);

            //when
            List<UserRecommendationResponse> result = recommendationService.getRecommendations(userId, 5);

            //then
            assertThat(result).isEqualTo(cachedRecommendations);
            verify(cacheManager, times(1)).getJson(eq(cacheKey), any(TypeReference.class));
            verifyNoInteractions(userInterestRepository, followReadRepository, userRepository);
        }

    }

    @Nested
    @DisplayName("generateRecommendations 메서드는")
    class DescribeGenerateRecommendations {

        @Test
        @DisplayName("사용자의 관심사가 없으면 빈 리스트를 반환한다.")
        void itReturnsEmptyListIfNoInterests() {
            // Given
            String userId = "user123";
            when(userInterestRepository.findByUserId(UserId.of(userId))).thenReturn(Collections.emptyList());

            // When
            List<UserRecommendationResponse> result = recommendationService.generateRecommendations(userId, 10);

            // Then
            assertThat(result).isEmpty();
            verify(userInterestRepository, times(1)).findByUserId(UserId.of(userId));
            verifyNoInteractions(followReadRepository, userRepository);
        }

        @Test
        @DisplayName("추천할 사용자가 없으면 빈 리스트를 반환한다.")
        void itReturnsEmptyListIfNoRecommendedUsers() {
            // Given
            String userId = "user123";
            when(userInterestRepository.findByUserId(UserId.of(userId))).thenReturn(Collections.emptyList());
            when(userInterestRepository.findUserIdsByInterestCategories(anyList(), eq(userId))).thenReturn(Collections.emptyList());

            // When
            List<UserRecommendationResponse> result = recommendationService.generateRecommendations(userId, 10);

            // Then
            assertThat(result).isEmpty();
            verify(userInterestRepository, times(1)).findByUserId(UserId.of(userId));
            verify(userInterestRepository, times(1)).findUserIdsByInterestCategories(anyList(), eq(userId));
            verifyNoInteractions(followReadRepository, userRepository);
        }

        @Test
        @DisplayName("자기 자신은 추천 리스트에서 제외된다.")
        void itExcludesSelfFromRecommendations() {
            // Given
            String userId = "user123";
            List<UserInterest> userInterests = List.of(
                    UserInterest.create(CategoryId.of(1L), UserId.of(userId)),
                    UserInterest.create(CategoryId.of(2L), UserId.of(userId))
            );
            List<Long> categoryIds = List.of(1L, 2L);
            List<String> filteredUserIds = List.of("user124", "user125");

            // 관심사 조회
            when(userInterestRepository.findByUserId(UserId.of(userId))).thenReturn(userInterests);

            // 관심사 기반 필터링
            when(userInterestRepository.findUserIdsByInterestCategories(categoryIds, userId)).thenReturn(filteredUserIds);

            // 팔로워 수 및 사용자 정보 조회
            when(followReadRepository.getFollowerCounts(filteredUserIds)).thenReturn(Map.of(
                    "user124", 10L,
                    "user125", 5L
            ));
            when(userRepository.findUsersByIds(filteredUserIds)).thenReturn(List.of(
                    User.transform("user124", "user124@gmail.com","encodedPassword1","nickname124", null, "description1", UserRole.ROLE_USER),
                    User.transform("user125", "user125@gmail.com","encodedPassword2","nickname125", null, "description2", UserRole.ROLE_USER)
            ));

            // When
            List<UserRecommendationResponse> result = recommendationService.generateRecommendations(userId, 10);

            // Then
            assertThat(result).hasSize(2);
            assertThat(result).extracting("userId").doesNotContain("user123");
            assertThat(result.get(0).followerCount()).isEqualTo(10L);
            assertThat(result.get(1).followerCount()).isEqualTo(5L);

            // 메서드 호출 검증
            verify(userInterestRepository, times(1)).findByUserId(UserId.of(userId));
            verify(userInterestRepository, times(1)).findUserIdsByInterestCategories(categoryIds, userId);
            verify(followReadRepository, times(1)).getFollowerCounts(filteredUserIds);
            verify(userRepository, times(1)).findUsersByIds(filteredUserIds);
        }
    }
}