package com.wsws.moduleapplication.service.follow;

import com.wsws.moduleapplication.dto.follow.FollowServiceRequestDto;
import com.wsws.moduledomain.follow.Follow;
import com.wsws.moduledomain.follow.repo.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;

    //팔로우
    public void followUser(FollowServiceRequestDto followServiceRequestDto) {
        if(followRepository.findByFollowerIdAndFolloweeId(followServiceRequestDto.followerId(), followServiceRequestDto.followeeId()).isPresent()) {
            throw new IllegalArgumentException("이미 팔로우한 사용자입니다.");
        }
        Follow follow = Follow.create(followServiceRequestDto.followerId(),followServiceRequestDto.followeeId());
        followRepository.save(follow);
    }


    //언팔로우
    public void unfollowUser(FollowServiceRequestDto followServiceRequestDto) {
        Follow follow = followRepository.findByFollowerIdAndFolloweeId(followServiceRequestDto.followerId(), followServiceRequestDto.followeeId())
                .orElseThrow(() -> new IllegalArgumentException("팔로우 관계를 찾을 수 없습니다."));
        followRepository.delete(follow);
    }

    //팔로우 여부 체크
    public boolean isFollowing(String followerId, String followeeId) {
        return followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId).isPresent();
    }


}

