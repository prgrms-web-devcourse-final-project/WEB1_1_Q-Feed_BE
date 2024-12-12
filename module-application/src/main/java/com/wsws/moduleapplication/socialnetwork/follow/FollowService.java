package com.wsws.moduleapplication.socialnetwork.follow;

import com.wsws.moduleapplication.socialnetwork.follow.dto.FollowServiceRequestDto;
import com.wsws.moduleapplication.socialnetwork.exception.AlreadyFollowedException;
import com.wsws.moduleapplication.socialnetwork.exception.FollowNotFoundException;
import com.wsws.moduleapplication.usercontext.user.exception.UserNotFoundException;
import com.wsws.moduledomain.socialnetwork.follow.aggregate.Follow;
import com.wsws.moduledomain.socialnetwork.follow.repo.FollowRepository;
import com.wsws.moduledomain.notification.Notification;
import com.wsws.moduledomain.notification.repo.NotificationRepository;
import com.wsws.moduledomain.usercontext.user.aggregate.User;
import com.wsws.moduledomain.usercontext.user.repo.UserRepository;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import com.wsws.moduleexternalapi.fcm.dto.fcmRequestDto;
import com.wsws.moduleexternalapi.fcm.service.FcmService;
import com.wsws.moduleexternalapi.fcm.util.FcmType;
import com.wsws.moduledomain.cache.CacheManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final NotificationRepository notificationRepository;
    private final FcmService fcmService;
    private final CacheManager cacheManager;

    // 팔로우
    @Transactional
    public void followUser(FollowServiceRequestDto followServiceRequestDto) {
        String followerId = followServiceRequestDto.followerId();
        String followeeId = followServiceRequestDto.followeeId();

        // 이미 팔로우된 상태인지 확인
        if (followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId).isPresent()) {
            throw AlreadyFollowedException.EXCEPTION;
        }

        // Follow 엔티티 생성 및 저장
        Follow follow = Follow.create(followerId, followeeId);
        followRepository.save(follow);

        // 캐시 무효화
        evictFollowerFollowingCache(followerId, followeeId);

        // 알림 저장 및 FCM 전송
//        sendFollowNotification(followerId, followeeId);
    }

    @Transactional
    public void unfollowUser(FollowServiceRequestDto followServiceRequestDto) {
        String followerId = followServiceRequestDto.followerId();
        String followeeId = followServiceRequestDto.followeeId();

        // Follow 관계 확인 및 삭제
        Follow follow = followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)
                .orElseThrow(() -> FollowNotFoundException.EXCEPTION);
        followRepository.delete(follow);

        // 팔로워/팔로잉 수 감소
        evictFollowerFollowingCache(followerId, followeeId);
    }

    public boolean isFollowing(String followerId, String followeeId) {
        return followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId).isPresent();
    }

    private void sendFollowNotification(String followerId, String followeeId) {
        User followerUser = userRepository.findById(UserId.of(followerId))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
        User followeeUser = userRepository.findById(UserId.of(followeeId))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        String title = fcmService.makeFcmTitle(FcmType.FOLLOW.getType());
        String body = fcmService.makeFollowBody(followeeUser.getNickname().getValue(), FcmType.FOLLOW.getType());
        fcmRequestDto fcmDTO = fcmService.makeFcmDTO(title, body);

        // 알림 저장
        Notification notification = Notification.builder()
                .type(FcmType.FOLLOW.getType())
                .sender(followeeUser.getNickname().getValue())
                .recipient(followerUser.getNickname().getValue())
                .build();
        notificationRepository.save(notification);

        // FCM 전송
        fcmService.fcmSend(followerUser.getNickname().getValue(), fcmDTO);
    }

    //캐시 삭제 -> 업데이트 되면 이전의 캐시는 의미가 없어짐
    private void evictFollowerFollowingCache(String followerId, String followeeId) {
        cacheManager.evict("user:" + followerId + ":followingCount");
        cacheManager.evict("user:" + followeeId + ":followerCount");
    }
}
