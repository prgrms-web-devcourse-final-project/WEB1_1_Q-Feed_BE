package com.wsws.moduledomain.user.repo;

import com.wsws.moduledomain.user.Like;
import com.wsws.moduledomain.user.User;

public interface LikeRepository {
    /**
     * Like 저장
     */
    Like save(Like like, User user);

    /**
     * 특정 사용자가 특정 글에 좋아요를 눌렀는지
     */
    boolean existsByTargetIdAndUserId(Long targetId, String userId);

    /**
     * 특정 사용자가 특정 글에 누른 좋아요 정보 삭제
     */
    void deleteByTargetIdAndUserId(Long targetId, String userId);

}