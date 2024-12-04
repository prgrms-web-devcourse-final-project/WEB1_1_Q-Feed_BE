package com.wsws.moduleinfra.repo.group;

import com.wsws.moduledomain.group.GroupPost;
import com.wsws.moduledomain.group.dto.GroupPostDto;
import com.wsws.moduledomain.group.repo.GroupPostRepository;
import com.wsws.moduleinfra.entity.group.GroupPostEntity;
import com.wsws.moduleinfra.repo.group.mapper.GroupPostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GroupPostRepositoryImpl implements GroupPostRepository {

    private final JpaGroupPostRepository jpaGroupPostRepository;

    @Override
    public void save(GroupPost groupPost) {
        GroupPostEntity groupPostEntity = GroupPostMapper.toEntity(groupPost);
        jpaGroupPostRepository.save(groupPostEntity);
    }

    @Override
    public void delete(Long groupPostId) {
        jpaGroupPostRepository.deleteById(groupPostId);
    }

    //상세조회
//    @Override
//    public Optional<GroupPost> findById(Long groupPostId) {
//        return jpaGroupPostRepository.findById(groupPostId)
//                .map(GroupPostMapper::toDomain);
//    }

    @Override
    public List<GroupPostDto> findByGroupId(String groupId) {
       return jpaGroupPostRepository.findByGroupId(groupId);
    }

    @Override
    public void delete(GroupPost groupPost) {
        GroupPostEntity groupPostEntity = GroupPostMapper.toEntity(groupPost);
        jpaGroupPostRepository.delete(groupPostEntity);

    }

    @Override
    public Optional<Object> findById(Long groupPostId) {
        return Optional.of(jpaGroupPostRepository.findById(groupPostId));
    }
}
