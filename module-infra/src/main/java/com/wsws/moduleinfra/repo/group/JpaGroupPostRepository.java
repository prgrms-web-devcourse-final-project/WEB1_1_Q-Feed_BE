package com.wsws.moduleinfra.repo.group;

import com.wsws.moduledomain.group.dto.GroupPostDto;
import com.wsws.moduleinfra.entity.group.GroupPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaGroupPostRepository extends JpaRepository<GroupPostEntity, Long> {

    //그룹 ID로 게시글 조회
    @Query("SELECT new com.wsws.moduledomain.group.dto.GroupPostDto(" +
            "g.groupPostId, u.nickname, u.profileImage, g.content, g.createAt, " +
            "CASE WHEN f IS NOT NULL THEN true ELSE false END) " +
            "FROM GroupPostEntity g " +
            "JOIN UserEntity u ON u.id = g.userId " +
            "LEFT JOIN FollowEntity f ON f.id.followeeId = g.userId AND f.id.followerId = :currentUserId " +
            "WHERE g.groupId = :groupId")
    List<GroupPostDto> findByGroupId(@Param("groupId") String groupId);

    //postId로 게시물 상세 조회


}
