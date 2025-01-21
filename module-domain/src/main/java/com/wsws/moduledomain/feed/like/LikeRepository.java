package com.wsws.moduledomain.feed.like;

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
     * 특정 사용자가 누른 좋아요 목록 가져오기
     */
    List<Like> findByUserId(String userId);
    /**
     *  특정 유저가 특정 답변들에 남겨진 좋아요 목록 가져오기
     */
    List<Like> findByTargetIdsInAndTargetTypeAndUserId(List<Long> answerIds, TargetType targetType, String userId);

    /**
     * 특정 사용자가 특정 글에 좋아요를 눌렀는지
     */
    boolean existsByUserEntityIdAndTargetIdAndTargetType(String userId, Long targetId, TargetType targetType);
    /**
     * 데이터베이스 즉시 반영하기
     */
    void flush();

}
