package com.wsws.moduleapplication.follow.service;

import com.wsws.moduleapplication.follow.dto.FollowServiceRequestDto;
import com.wsws.moduleapplication.follow.exception.AlreadyFollowedException;
import com.wsws.moduleapplication.follow.exception.FollowNotFoundException;
import com.wsws.moduledomain.follow.Follow;
import com.wsws.moduledomain.follow.repo.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;

    // 팔로우
    public void followUser(FollowServiceRequestDto followServiceRequestDto) {
        if (followRepository.findByFollowerIdAndFolloweeId(followServiceRequestDto.followerId(), followServiceRequestDto.followeeId()).isPresent()) {
            throw AlreadyFollowedException.EXCEPTION;
        }
        Follow follow = Follow.create(followServiceRequestDto.followerId(), followServiceRequestDto.followeeId());
        followRepository.save(follow);
    }

    // 언팔로우
    public void unfollowUser(FollowServiceRequestDto followServiceRequestDto) {
        Follow follow = followRepository.findByFollowerIdAndFolloweeId(followServiceRequestDto.followerId(), followServiceRequestDto.followeeId())
                .orElseThrow(() -> FollowNotFoundException.EXCEPTION);
        followRepository.delete(follow);
    }

    // 팔로우 여부 체크
    public boolean isFollowing(String followerId, String followeeId) {
        return followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId).isPresent();
    }
}
