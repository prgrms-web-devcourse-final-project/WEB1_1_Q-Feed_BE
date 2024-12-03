package com.wsws.moduleinfra.repo.group;

import com.wsws.moduleinfra.entity.group.GroupPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaGroupPostRepository  extends JpaRepository<GroupPostEntity, Long> {

    //그룹 ID로 게시글 조회
    @Query("SELECT g FROM GroupPostEntity g WHERE g.groupId = :groupId")
    List<GroupPostEntity> findByGroupId(@Param("groupId") String groupId);
}
