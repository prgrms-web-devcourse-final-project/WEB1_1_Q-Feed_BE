package com.wsws.moduleapi.mapper;

import com.wsws.moduleapi.dto.follow.FollowRequestDto;
import com.wsws.moduleapi.dto.follow.FollowResponseDto;
import com.wsws.moduleapplication.dto.follow.FollowServiceRequestDto;
import com.wsws.moduleapplication.dto.follow.FollowServiceResponseDto;
import com.wsws.moduledomain.follow.Follow;

public class FollowMapper {

    public static FollowServiceRequestDto toServiceRequestDto(FollowRequestDto requestDto) {
        return new FollowServiceRequestDto(
                requestDto.followerId(),
                requestDto.followeeId()
        );
    }

    public static FollowResponseDto toControllerResponseDto(FollowServiceResponseDto serviceDto) {
        return new FollowResponseDto(
                serviceDto.userId(),
                serviceDto.nickname(),
                serviceDto.profileImage()
        );
    }


}
