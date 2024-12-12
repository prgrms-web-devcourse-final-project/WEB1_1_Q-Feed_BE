package com.wsws.moduledomain.socialnetwork.follow.vo;

import lombok.Value;

@Value
public class UserRecommendation {
    String userId;
    String nickname;
    String profileimage;
    Long followerCount;
}
