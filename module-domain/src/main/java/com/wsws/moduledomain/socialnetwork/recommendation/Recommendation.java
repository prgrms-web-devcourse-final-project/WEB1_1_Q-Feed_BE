package com.wsws.moduledomain.socialnetwork.recommendation;

import lombok.Value;

@Value
public class Recommendation {
    String userId;
    String nickname;
    String profileimage;
    Long followerCount;

    public static Recommendation of(String userId, String nickname, String profileImage, Long followerCount) {
        return new Recommendation(userId, nickname, profileImage, followerCount);
    }
}
