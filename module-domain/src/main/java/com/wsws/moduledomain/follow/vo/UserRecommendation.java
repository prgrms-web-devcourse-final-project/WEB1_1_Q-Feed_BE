package com.wsws.moduledomain.follow.vo;

import lombok.Value;

@Value
public class UserRecommendation {
    String userId;
    String nickname;
    String profileimage;
    Long followerCount;
}
