package com.wsws.moduleapplication.socialnetwork.follow;

import com.wsws.moduleapplication.notification.service.NotificationService;
import com.wsws.moduleapplication.socialnetwork.follow.dto.FollowServiceRequestDto;
import com.wsws.moduleapplication.socialnetwork.exception.AlreadyFollowedException;
import com.wsws.moduleapplication.socialnetwork.exception.FollowNotFoundException;
import com.wsws.moduledomain.socialnetwork.follow.aggregate.Follow;
import com.wsws.moduledomain.socialnetwork.follow.repo.FollowRepository;
import com.wsws.moduleexternalapi.fcm.util.FcmType;
import com.wsws.moduledomain.cache.CacheManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final CacheManager cacheManager;
    private final NotificationService notificationService;

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

        // 알림 전송 및 저장
        notificationService.sendNotification(
                followerId,
                followeeId,
                null,
                null,
                null,
                "/profile/users/" + followerId,
                FcmType.FOLLOW
        );
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

    //캐시 삭제 -> 업데이트 되면 이전의 캐시는 의미가 없어짐
    private void evictFollowerFollowingCache(String followerId, String followeeId) {
        cacheManager.evict("user:" + followerId + ":followingCount");
        cacheManager.evict("user:" + followeeId + ":followerCount");
    }
}
