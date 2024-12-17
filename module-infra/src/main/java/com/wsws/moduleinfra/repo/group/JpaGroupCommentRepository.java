package com.wsws.moduleinfra.repo.group;

import com.wsws.moduledomain.group.dto.GroupCommentDto;
import com.wsws.moduleinfra.entity.group.GroupCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaGroupCommentRepository extends JpaRepository<GroupCommentEntity, Long> {

    // POSTID로 댓글 조회
    @Query("SELECT new com.wsws.moduledomain.group.dto.GroupCommentDto( " +
            "c.groupCommentId, c.content, c.createdAt, c.userId, u.nickname, c.likeCount, c.groupPost.groupPostId) " +
            "FROM GroupCommentEntity c " +
            "JOIN UserEntity u ON u.id = c.userId " +
            "WHERE c.groupPost.groupPostId = :groupPostId")
    List<GroupCommentDto> findByGroupPostId(@Param("groupPostId") Long groupPostId);

//    @Query("SELECT new com.wsws.moduledomain.group.dto.GroupCommentDto( " +
//            "c.groupCommentId, c.content, c.createdAt, c.userId, c.likeCount, c.groupPost.groupPostId) " +
//            "FROM GroupCommentEntity c " +
//            "WHERE c.groupPost.groupPostId = :groupPostId")
//    List<GroupCommentDto> findByGroupPostId(@Param("groupPostId") Long groupPostId);

}
