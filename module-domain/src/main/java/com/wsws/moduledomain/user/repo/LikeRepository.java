package com.wsws.moduledomain.user.repo;

import com.wsws.moduledomain.user.Like;
import com.wsws.moduledomain.user.User;
import com.wsws.moduledomain.user.vo.TargetType;

import java.util.List;

public interface LikeRepository {
    /**
     * Like 저장
     */
    Like save(Like like);

    /**
     * 특정 사용자가 특정 글에 좋아요를 눌렀는지
     */
    boolean existsByTargetIdAndUserIdAndTargetType(Long targetId, String userId, TargetType targetType);

    /**
     * 특정 사용자가 특정 글에 누른 좋아요 정보 삭제
     */
    void deleteByTargetIdAndUserId(Long targetId, String userId);

    /**
     * 특정 사용자가 좋아요 누른 글 목록 가져오기
     */
    List<Like> findByUserId(String userId);

}
