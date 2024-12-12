package com.wsws.moduleapplication.usercontext.user.service;

import com.wsws.moduleapplication.usercontext.user.dto.FullUserProfileResponse;
import com.wsws.moduleapplication.usercontext.user.dto.UserProfileResponse;
import com.wsws.moduledomain.cache.CacheManager;
import com.wsws.moduledomain.usercontext.user.aggregate.User;
import com.wsws.moduledomain.usercontext.user.repo.UserRepository;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import com.wsws.moduledomain.follow.repo.FollowReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;
    private final FollowReadRepository followReadRepository;
    private final CacheManager cacheManager;

    // UserProfile 캐싱 처리
    public UserProfileResponse getUserProfile(String userId) {
        String profileCacheKey = "user:" + userId + ":profile";

        UserProfileResponse cachedProfile = cacheManager.get(profileCacheKey, UserProfileResponse.class);
        if (cachedProfile != null) {
            return cachedProfile;
        }

        // DB 조회
        User user = userRepository.findById(UserId.of(userId))
                .orElseThrow(() -> new IllegalStateException("User not found."));
        UserProfileResponse profile = new UserProfileResponse(user);

        // 캐시에 저장
        cacheManager.set(profileCacheKey, profile, 24 * 60); // 24시간 TTL
        return profile;
    }

    // Full UserProfile 통합 처리
    public FullUserProfileResponse getFullUserProfile(String userId) {
        UserProfileResponse profile = getUserProfile(userId); // 프로필 데이터
        long followerCount = getFollowerCount(userId);        // 팔로워 수
        long followingCount = getFollowingCount(userId);      // 팔로잉 수

        return new FullUserProfileResponse(profile, followerCount, followingCount);
    }

    // Follower Count 캐싱 처리
    public long getFollowerCount(String userId) {
        String followerCountCacheKey = "user:" + userId + ":followerCount";

        Integer cachedCount = cacheManager.get(followerCountCacheKey, Integer.class);
        if (cachedCount != null) {
            return cachedCount;
        }

        // DB 조회 및 캐시 저장
        long followerCount = followReadRepository.countFollowersByUserId(userId);
        cacheManager.set(followerCountCacheKey, followerCount, 10); // 10분 TTL
        return followerCount;
    }

    // Following Count 캐싱 처리
    public long getFollowingCount(String userId) {
        String followingCountCacheKey = "user:" + userId + ":followingCount";

        Integer cachedCount = cacheManager.get(followingCountCacheKey, Integer.class);
        if (cachedCount != null) {
            return cachedCount;
        }

        // DB 조회 및 캐시 저장
        long followingCount = followReadRepository.countFollowingsByUserId(userId);
        cacheManager.set(followingCountCacheKey, followingCount, 10); // 10분 TTL
        return followingCount;
    }


}