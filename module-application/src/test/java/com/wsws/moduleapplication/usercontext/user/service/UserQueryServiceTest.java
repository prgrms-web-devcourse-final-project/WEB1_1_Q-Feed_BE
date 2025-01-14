package com.wsws.moduleapplication.usercontext.user.service;

import com.wsws.moduleapplication.usercontext.user.dto.FullUserProfileResponse;
import com.wsws.moduleapplication.usercontext.user.dto.UserProfileResponse;
import com.wsws.moduledomain.cache.CacheManager;
import com.wsws.moduledomain.socialnetwork.follow.repo.FollowReadRepository;
import com.wsws.moduledomain.usercontext.user.aggregate.User;
import com.wsws.moduledomain.usercontext.user.encoder.PasswordEncoder;
import com.wsws.moduledomain.usercontext.user.repo.UserRepository;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserQueryServiceTest {

    private static final String USER_ID       = "user1";
    private static final String EMAIL         = "tester1@gmail.com";
    private static final String RAW_PASSWORD  = "Tester123!";
    private static final String NICKNAME      = "testUser1";
    private static final String PROFILE_IMAGE = null;
    private static final String DESCRIPTION   = "Hi! I am TESTUSER1";

    @Mock
    private UserRepository userRepository;

    @Mock
    private FollowReadRepository followReadRepository;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserQueryService userQueryService;

    @Nested
    @DisplayName("getUserProfile 메서드는")
    class DescribeGetUserProfile {

        @Test
        @DisplayName("캐시에 값이 존재하면 캐시에서 반환하고 DB 조회는 하지 않는다.")
        void itReturnsCachedProfileWithoutDBLookup() {
            // given
            UserProfileResponse cachedProfile = new UserProfileResponse(
                    USER_ID, EMAIL, NICKNAME, "", DESCRIPTION
            );
            when(cacheManager.get("user:" + USER_ID + ":profile", UserProfileResponse.class))
                    .thenReturn(cachedProfile);

            // when
            UserProfileResponse result = userQueryService.getUserProfile(USER_ID);

            // then
            assertThat(result).isEqualTo(cachedProfile);
            verifyNoInteractions(userRepository); // DB 조회가 일어나지 않음을 검증
        }

        @Test
        @DisplayName("캐시에 값이 없으면 DB를 조회하고, 결과를 캐시에 저장한다.")
        void itFetchesFromDBAndSavesToCache() {
            // given
            when(cacheManager.get("user:" + USER_ID + ":profile", UserProfileResponse.class))
                    .thenReturn(null);

            // User 엔티티 생성
            User user = User.create(EMAIL, RAW_PASSWORD, NICKNAME, PROFILE_IMAGE, DESCRIPTION, passwordEncoder);

            // DB에서 찾으면 Optional.of(user)
            when(userRepository.findById(UserId.of(USER_ID)))
                    .thenReturn(Optional.of(user));

            // when
            UserProfileResponse result = userQueryService.getUserProfile(USER_ID);

            // then
            // 반환값 검증
            assertThat(result).isNotNull();
            assertThat(result.userId()).isNotNull();
            assertThat(result.email()).isEqualTo(EMAIL);
            assertThat(result.nickname()).isEqualTo(NICKNAME);
            assertThat(result.profileImage()).isNullOrEmpty();
            assertThat(result.description()).isEqualTo(DESCRIPTION);

            // 캐시에 set 되었는지 확인
            verify(cacheManager, times(1))
                    .set(eq("user:" + USER_ID + ":profile"), any(UserProfileResponse.class), eq(1440L));
            // 24시간(=1440분) TTL
        }

        @Test
        @DisplayName("DB에서 User를 찾지 못하면 예외를 발생시킨다.")
        void itThrowsExceptionIfUserNotFound() {
            // given
            when(cacheManager.get("user:" + USER_ID + ":profile", UserProfileResponse.class))
                    .thenReturn(null);

            when(userRepository.findById(UserId.of(USER_ID)))
                    .thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userQueryService.getUserProfile(USER_ID))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("User not found.");
        }
    }

    // -------------------------------------------------------------------------
    // getFullUserProfile 테스트
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("getFullUserProfile 메서드는")
    class DescribeGetFullUserProfile {

        @Test
        @DisplayName("UserProfile + FollowerCount + FollowingCount 를 묶어서 반환한다.")
        void itReturnsFullUserProfile() {
            // given
            // 캐시에는 없다고 가정 (DB 조회 유도)
            when(cacheManager.get("user:" + USER_ID + ":profile", UserProfileResponse.class))
                    .thenReturn(null);

            User createTestUser = User.create(EMAIL, RAW_PASSWORD, NICKNAME, PROFILE_IMAGE, DESCRIPTION, passwordEncoder);

            // DB에서 user 조회 가능
            when(userRepository.findById(UserId.of(USER_ID)))
                    .thenReturn(Optional.of(createTestUser));

            when(followReadRepository.countFollowersByUserId(USER_ID))
                    .thenReturn(5);
            when(followReadRepository.countFollowingsByUserId(USER_ID))
                    .thenReturn(3);

            // when
            FullUserProfileResponse result = userQueryService.getFullUserProfile(USER_ID);

            // then
            assertThat(result).isNotNull();
            assertThat(result.followerCount()).isEqualTo(5L);
            assertThat(result.followingCount()).isEqualTo(3L);
            verify(followReadRepository).countFollowersByUserId(USER_ID);
            verify(followReadRepository).countFollowingsByUserId(USER_ID);
        }
    }

    // -------------------------------------------------------------------------
    // getFollowerCount 테스트
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("getFollowerCount 메서드는")
    class DescribeGetFollowerCount {

        @Test
        @DisplayName("캐시에 값이 있으면 캐시에서 반환하고 DB는 조회하지 않는다.")
        void itReturnsCachedCountWithoutDBLookup() {
            // given
            when(cacheManager.get("user:" + USER_ID + ":followerCount", Integer.class))
                    .thenReturn(10);

            // when
            long followerCount = userQueryService.getFollowerCount(USER_ID);

            // then
            assertThat(followerCount).isEqualTo(10);
            verifyNoInteractions(followReadRepository);
        }

        @Test
        @DisplayName("캐시에 값이 없으면 DB에서 조회하고 캐시에 저장한다.")
        void itFetchesCountFromDBAndSavesToCache() {
            // given
            when(cacheManager.get("user:" + USER_ID + ":followerCount", Integer.class))
                    .thenReturn(null);

            when(followReadRepository.countFollowersByUserId(USER_ID))
                    .thenReturn(15);

            // when
            long followerCount = userQueryService.getFollowerCount(USER_ID);

            // then
            assertThat(followerCount).isEqualTo(15L);
            verify(cacheManager, times(1))
                    .set("user:" + USER_ID + ":followerCount", 15L, 10L);
        }
    }

    // -------------------------------------------------------------------------
    // getFollowingCount 테스트
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("getFollowingCount 메서드는")
    class DescribeGetFollowingCount {

        @Test
        @DisplayName("캐시에 값이 있으면 캐시에서 반환하고 DB는 조회하지 않는다.")
        void itReturnsCachedCountWithoutDBLookup() {
            // given
            when(cacheManager.get("user:" + USER_ID + ":followingCount", Integer.class))
                    .thenReturn(8);

            // when
            long followingCount = userQueryService.getFollowingCount(USER_ID);

            // then
            assertThat(followingCount).isEqualTo(8);
            verifyNoInteractions(followReadRepository);
        }

        @Test
        @DisplayName("캐시에 값이 없으면 DB에서 조회하고 캐시에 저장한다.")
        void itFetchesCountFromDBAndSavesToCache() {
            // given
            when(cacheManager.get("user:" + USER_ID + ":followingCount", Integer.class))
                    .thenReturn(null);

            when(followReadRepository.countFollowingsByUserId(USER_ID))
                    .thenReturn(12);

            // when
            long followingCount = userQueryService.getFollowingCount(USER_ID);

            // then
            assertThat(followingCount).isEqualTo(12L);
            verify(cacheManager, times(1))
                    .set("user:" + USER_ID + ":followingCount", 12L, 10L);
        }
    }


}