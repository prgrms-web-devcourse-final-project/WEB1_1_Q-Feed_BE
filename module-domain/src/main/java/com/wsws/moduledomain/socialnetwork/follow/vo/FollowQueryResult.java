package com.wsws.moduledomain.socialnetwork.follow.vo;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class FollowQueryResult {
    String userId;          // 팔로워 또는 팔로이의 ID
    String nickname;        // 닉네임
    String profileImage;    // 프로필 이미지 URL
    LocalDateTime createdAt; // 팔로우 관계 생성일
}
