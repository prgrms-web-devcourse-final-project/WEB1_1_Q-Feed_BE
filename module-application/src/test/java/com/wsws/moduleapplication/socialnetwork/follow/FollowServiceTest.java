package com.wsws.moduleapplication.socialnetwork.follow;

import com.wsws.moduleapplication.socialnetwork.exception.AlreadyFollowedException;
import com.wsws.moduleapplication.socialnetwork.exception.FollowNotFoundException;
import com.wsws.moduleapplication.socialnetwork.follow.dto.FollowServiceRequestDto;
import com.wsws.moduleapplication.socialnetwork.follow.event.FollowCreatedEvent;
import com.wsws.moduleapplication.socialnetwork.follow.event.FollowDeletedEvent;
import com.wsws.moduleapplication.socialnetwork.follow.event.FollowEvent;
import com.wsws.moduledomain.socialnetwork.follow.aggregate.Follow;
import com.wsws.moduledomain.socialnetwork.follow.repo.FollowRepository;
import com.wsws.moduleexternalapi.fcm.util.FcmType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.NotExtensible;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {
    @Mock
    private FollowRepository followRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @InjectMocks
    private FollowService followService;

    @Nested
    @DisplayName("followUser 메서드는")
    class DescribeFollowUser {

        @Test
        @DisplayName("FollowServiceRequestDto를 받아서 팔로우를 생성할 수 있다.")
        void itfollowUser() {
            // given
            String followerId = "followerId";
            String followeeId = "followeeId";
            FollowServiceRequestDto followServiceRequestDto = new FollowServiceRequestDto(followerId, followeeId);

            when(followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)).thenReturn(Optional.empty());

            // when
            followService.followUser(followServiceRequestDto);

            // then
            verify(followRepository, times(1)).save(any(Follow.class)); // 팔로우 저장 확인
            verify(eventPublisher, times(1)).publishEvent(new FollowCreatedEvent(followerId, followeeId)); // 캐시 무효화 이벤트 확인
            verify(eventPublisher, times(1)).publishEvent(new FollowEvent(followerId, followeeId, FcmType.FOLLOW)); // 알림 이벤트 확인
        }

        @Test
        @DisplayName("이미 팔로우된 상태라면 예외를 던진다")
        void throwsExceptionIfAlreadyFollowed() {
            // given
            String followerId = "followerId";
            String followeeId = "followeeId";
            FollowServiceRequestDto followServiceRequestDto = new FollowServiceRequestDto(followerId, followeeId);

            when(followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId))
                    .thenReturn(Optional.of(mock(Follow.class)));

            // when & then
            assertThatThrownBy(() -> followService.followUser(followServiceRequestDto))
                    .isInstanceOf(AlreadyFollowedException.class);

            verify(followRepository, never()).save(any(Follow.class));
            verify(eventPublisher, never()).publishEvent(any());
        }
    }

    @Nested
    @DisplayName("unfollowerUser 메서드는")
    class DescribeUnfollowUser {

        @Test
        @DisplayName("FollowServiceRequestDto를 받아서 팔로우를 해제할 수 있다")
        void itunfollowerUser() {
            //given
            String followerId = "followerId";
            String followeeId = "followeeId";
            FollowServiceRequestDto followServiceRequestDto = new FollowServiceRequestDto(followerId, followeeId);

            Follow follow = mock(Follow.class);

            when(followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)).thenReturn(Optional.of(follow));

            //when
            followService.unfollowUser(followServiceRequestDto);

            //then
            verify(followRepository, times(1)).delete(follow);
            verify(eventPublisher, times(1)).publishEvent(new FollowDeletedEvent(followerId, followeeId));

        }

        @Test
        @DisplayName("팔로우가 없으면 FollowNotFoundException을 던진다.")
        void throwsExceptionIfFollowNotFound() {
            //given
            String followerId = "followerId";
            String followeeId = "followeeId";
            FollowServiceRequestDto followServiceRequestDto = new FollowServiceRequestDto(followerId, followeeId);

            when(followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)).thenReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> followService.unfollowUser(followServiceRequestDto))
                    .isInstanceOf(FollowNotFoundException.class);

            verify(followRepository, never()).delete(any(Follow.class)); // 삭제 호출되지 않음
            verify(eventPublisher, never()).publishEvent(any()); // 이벤트 발행되지 않음
        }
    }

    @Nested
    @DisplayName("isFollowing 메서드는")
    class DescribeIsFollowing {

        @Test
        @DisplayName("팔로우 관계가 존재하면 true를 반환한다.")
        void itReturnsTrueIfFollowExists() {
            String followerId = "followerId";
            String followeeId = "followeeId";

            when(followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)).thenReturn(Optional.of(mock(Follow.class)));
            //when
            boolean result = followService.isFollowing(followerId, followeeId);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("팔로우 관계가 존재하지 않으면 false를 반환한다.")
        void itReturnsFalseIfFollowNotExist() {
            String followerId = "followerId";
            String followeeId = "followeeId";

            when(followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)).thenReturn(Optional.empty());
            //when
            boolean result = followService.isFollowing(followerId, followeeId);

            assertThat(result).isFalse();
        }
    }

}