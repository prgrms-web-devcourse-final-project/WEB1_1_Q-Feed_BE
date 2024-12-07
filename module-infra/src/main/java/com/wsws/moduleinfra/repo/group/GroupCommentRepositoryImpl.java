package com.wsws.moduleinfra.repo.group;

import com.wsws.moduledomain.group.GroupComment;
import com.wsws.moduledomain.group.repo.GroupCommentRepository;
import com.wsws.moduleinfra.entity.group.GroupCommentEntity;
import com.wsws.moduleinfra.entity.group.GroupPostEntity;
import com.wsws.moduleinfra.repo.group.mapper.GroupCommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GroupCommentRepositoryImpl implements GroupCommentRepository {

    private final JpaGroupCommentRepository jpaGroupCommentRepository;
    private final JpaGroupPostRepository jpaGroupPostRepository;

    @Override
    public void save(GroupComment groupComment) {

        GroupPostEntity groupPostEntity = jpaGroupPostRepository.findById(groupComment.getGroupPostId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

        GroupCommentEntity groupCommentEntity = GroupCommentMapper.toEntity(groupComment);

        // 연관 관계 설정
        groupCommentEntity.setGroupPost(groupPostEntity);

        jpaGroupCommentRepository.save(groupCommentEntity);
    }

    @Override
    public Optional<GroupComment> findById(Long groupCommentId) {
        return jpaGroupCommentRepository.findById(groupCommentId)
                .map(GroupCommentMapper::toDomain); // Entity -> Domain 변환
    }
}
