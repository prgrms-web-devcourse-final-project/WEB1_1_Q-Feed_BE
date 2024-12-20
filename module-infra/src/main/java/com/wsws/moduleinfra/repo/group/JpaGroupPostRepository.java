package com.wsws.moduleinfra.repo.group;

import com.wsws.moduledomain.group.dto.GroupPostDetailDto;
import com.wsws.moduledomain.group.dto.GroupPostDto;
import com.wsws.moduleinfra.entity.group.GroupPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaGroupPostRepository extends JpaRepository<GroupPostEntity, Long> {

    // 그룹 ID로 게시글 조회
    @Query("SELECT new com.wsws.moduledomain.group.dto.GroupPostDto( " +
            "g.groupPostId, u.nickname, u.profileImage, g.content, g.createAt, g.userId, g.likeCount) " + // likeCount 추가
            "FROM GroupPostEntity g " +
            "JOIN UserEntity u ON u.id = g.userId " +
            "WHERE g.groupId = :groupId")
    List<GroupPostDto> findByGroupId(@Param("groupId") Long groupId);


    // postId로 게시물 상세 조회
    @Query("SELECT new com.wsws.moduledomain.group.dto.GroupPostDetailDto( " +
            "g.groupPostId, u.nickname, u.profileImage, g.content, g.createAt, g.userId, g.likeCount) " +
            "FROM GroupPostEntity g " +
            "JOIN UserEntity u ON u.id = g.userId " +
            "WHERE g.groupPostId = :groupPostId")
    Optional<GroupPostDetailDto> findByGroupPostId(@Param("groupPostId") Long groupPostId);




}
