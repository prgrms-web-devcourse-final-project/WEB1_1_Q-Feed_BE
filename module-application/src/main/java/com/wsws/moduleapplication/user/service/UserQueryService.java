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

        String cacheKey = "user:" + userId + ":profile";
        UserProfileResponse cachedProfile = cacheManager.get(cacheKey, UserProfileResponse.class);
        if (cachedProfile != null) {
            return cachedProfile;
        }

        //cache에 없으면
        User user = userRepository.findById(UserId.of(userId))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        int followerCount = followReadRepository.countFollowersByUserId(userId);
        int followingCount = followReadRepository.countFollowingsByUserId(userId);

        UserProfileResponse profileResponse = new UserProfileResponse(user, followerCount, followingCount);

        cacheManager.set(cacheKey, profileResponse, 600); // 10시간

        return profileResponse;
    }


}