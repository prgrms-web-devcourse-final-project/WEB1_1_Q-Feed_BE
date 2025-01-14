package com.wsws.moduleapplication.socialnetwork.follow;

import com.wsws.moduleapplication.socialnetwork.follow.dto.FollowServiceResponseDto;
import com.wsws.moduledomain.socialnetwork.follow.repo.FollowReadRepository;
import com.wsws.moduledomain.socialnetwork.follow.vo.FollowQueryResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowReadServiceTest {

    @Mock
    private  FollowReadRepository followReadRepository;

    @InjectMocks
    private FollowReadService followReadService;


    @Nested
    @DisplayName("getFollowersWithCursor 메서드는")
    class DescribeGetFollowersWithCursor {

        @Test
        @DisplayName("팔로워 목록을 반환한다")
        void itReturnsFollowers() {
            // given
            String followeeId = "followee-id";
            LocalDateTime cursor = LocalDateTime.now().minusDays(1);
            int size = 10;

            FollowQueryResult mockResult1 = new FollowQueryResult("user1", "nickname1", "image1", cursor.minusDays(1));
            FollowQueryResult mockResult2 = new FollowQueryResult("user2", "nickname2", "image2", cursor.minusDays(2));
            List<FollowQueryResult> mockResults = List.of(mockResult1, mockResult2);

            when(followReadRepository.findFollowersWithCursor(followeeId, cursor, size)).thenReturn(mockResults);

            // when
            List<FollowServiceResponseDto> followers = followReadService.getFollowersWithCursor(followeeId, cursor, size);

            // then
            assertThat(followers).hasSize(2);
            assertThat(followers).extracting("userId").containsExactly("user1", "user2");
            assertThat(followers).extracting("nickname").containsExactly("nickname1", "nickname2");
            assertThat(followers).extracting("profileImage").containsExactly("image1", "image2");

            verify(followReadRepository, times(1)).findFollowersWithCursor(followeeId, cursor, size);
        }

        @Test
        @DisplayName("커서가 null일 경우 현재 시간을 사용한다")
        void itUsesCurrentTimeIfCursorIsNull() {
            // given
            String followeeId = "followee-id";
            LocalDateTime currentTime = LocalDateTime.now();
            int size = 10;

            FollowQueryResult mockResult = new FollowQueryResult("user1", "nickname1", "image1", currentTime.minusDays(1));
            when(followReadRepository.findFollowersWithCursor(eq(followeeId), any(LocalDateTime.class), eq(size)))
                    .thenReturn(List.of(mockResult));

            // when
            List<FollowServiceResponseDto> followers = followReadService.getFollowersWithCursor(followeeId, null, size);

            // then
            assertThat(followers).hasSize(1);
            assertThat(followers.get(0).userId()).isEqualTo("user1");

            verify(followReadRepository, times(1)).findFollowersWithCursor(eq(followeeId), any(LocalDateTime.class), eq(size));
        }
    }

    @Nested
    @DisplayName("getFollowingsWithCursor 메서드는")
    class DescribeGetFollowingsWithCursor {

        @Test
        @DisplayName("팔로잉 목록을 반환한다")
        void itReturnsFollowings() {
            // given
            String followerId = "follower-id";
            LocalDateTime cursor = LocalDateTime.now().minusDays(1);
            int size = 10;

            FollowQueryResult mockResult1 = new FollowQueryResult("user1", "nickname1", "image1", cursor.minusDays(1));
            FollowQueryResult mockResult2 = new FollowQueryResult("user2", "nickname2", "image2", cursor.minusDays(2));
            List<FollowQueryResult> mockResults = List.of(mockResult1, mockResult2);

            when(followReadRepository.findFollowingsWithCursor(followerId, cursor, size)).thenReturn(mockResults);

            // when
            List<FollowServiceResponseDto> followings = followReadService.getFollowingsWithCursor(followerId, cursor, size);

            // then
            assertThat(followings).hasSize(2);
            assertThat(followings).extracting("userId").containsExactly("user1", "user2");
            assertThat(followings).extracting("nickname").containsExactly("nickname1", "nickname2");
            assertThat(followings).extracting("profileImage").containsExactly("image1", "image2");

            verify(followReadRepository, times(1)).findFollowingsWithCursor(followerId, cursor, size);
        }

        @Test
        @DisplayName("커서가 null일 경우 현재 시간을 사용한다")
        void itUsesCurrentTimeIfCursorIsNull() {
            // given
            String followerId = "follower-id";
            LocalDateTime currentTime = LocalDateTime.now();
            int size = 10;

            FollowQueryResult mockResult = new FollowQueryResult("user1", "nickname1", "image1", currentTime.minusDays(1));
            when(followReadRepository.findFollowingsWithCursor(eq(followerId), any(LocalDateTime.class), eq(size)))
                    .thenReturn(List.of(mockResult));

            // when
            List<FollowServiceResponseDto> followings = followReadService.getFollowingsWithCursor(followerId, null, size);

            // then
            assertThat(followings).hasSize(1);
            assertThat(followings.get(0).userId()).isEqualTo("user1");

            verify(followReadRepository, times(1)).findFollowingsWithCursor(eq(followerId), any(LocalDateTime.class), eq(size));
        }
    }
}