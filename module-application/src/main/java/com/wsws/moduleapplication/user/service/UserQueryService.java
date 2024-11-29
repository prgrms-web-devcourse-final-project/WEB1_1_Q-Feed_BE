package com.wsws.moduleapplication.user.service;

import com.wsws.moduleapplication.user.dto.UserProfileResponse;
import com.wsws.moduleapplication.user.exception.UserNotFoundException;
import com.wsws.moduledomain.cache.CacheManager;
import com.wsws.moduledomain.user.User;
import com.wsws.moduledomain.user.repo.UserRepository;
import com.wsws.moduledomain.user.vo.UserId;
import com.wsws.moduleinfra.repo.follow.FollowReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;
    private final FollowReadRepository followReadRepository;
    private final CacheManager cacheManager;

    public UserProfileResponse getUserProfile(String userId) {
        User user = userRepository.findById(UserId.of(userId))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        int followerCount = getFollowerCount(userId);
        int followingCount = getFollowingCount(userId);

        return new UserProfileResponse(user, followerCount, followingCount);
    }



    private int getFollowerCount(String userId) {
        String cacheKey = "user:" + userId + ":followerCount";
        Integer cachedCount = cacheManager.get(cacheKey);
        if (cachedCount != null) {
            return cachedCount;
        }
        int count = followReadRepository.countFollowersByUserId(userId);
        cacheManager.set(cacheKey, count, 10);
        return count;
    }

    private int getFollowingCount(String userId) {
        String cacheKey = "user:" + userId + ":followingCount";
        Integer cachedCount = cacheManager.get(cacheKey);
        if (cachedCount != null) {
            return cachedCount;
        }
        int count = followReadRepository.countFollowingsByUserId(userId);
        cacheManager.set(cacheKey, count, 10);
        return count;
    }
}
