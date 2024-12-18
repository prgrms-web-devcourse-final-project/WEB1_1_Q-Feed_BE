package com.wsws.moduleinfra.repo.group;

import com.wsws.moduledomain.group.GroupPost;
import com.wsws.moduledomain.group.dto.GroupPostDetailDto;
import com.wsws.moduledomain.group.dto.GroupPostDto;
import com.wsws.moduledomain.group.repo.GroupPostRepository;
import com.wsws.moduleinfra.entity.group.GroupPostEntity;
import com.wsws.moduleinfra.repo.group.mapper.GroupPostMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
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

    @Override
    public Optional<GroupPostDetailDto> findByGroupPostId(Long groupPostId) {
        return jpaGroupPostRepository.findByGroupPostId(groupPostId);
    }

    //상세조회 댓글 생성후 구현예정
//    @Override
//    public Optional<GroupPost> findById(Long groupPostId) {
//        return jpaGroupPostRepository.findById(groupPostId)
//                .map(GroupPostMapper::toDomain);
//    }

    @Override
    public List<GroupPostDto> findByGroupId(Long groupId) {
       return jpaGroupPostRepository.findByGroupId(groupId);
    }

//    @Override
//    public void delete(GroupPost groupPost) {
//        GroupPostEntity groupPostEntity = GroupPostMapper.toEntity(groupPost);
//        jpaGroupPostRepository.delete(groupPostEntity);
//
//    }

    @Override
    public Optional<GroupPost> findById(Long groupPostId) {
        return jpaGroupPostRepository.findById(groupPostId)
                .map(GroupPostMapper::toDomain);

    }


    @Override
    @Transactional
    public void edit(GroupPost groupPost) {

        // Null 체크
        if (groupPost.getGroupPostId() == null) {
            throw new IllegalArgumentException("GroupPostId가 null입니다.");
        }

        // 엔티티 조회
        GroupPostEntity groupPostEntity = jpaGroupPostRepository.findById(groupPost.getGroupPostId())
                .orElseThrow(() -> new RuntimeException("그룹 게시글을 찾을 수 없습니다."));

        // 좋아요 수정
        groupPostEntity.editEntity(
                groupPost.getLikeCount() // 좋아요 수 업데이트
        );

    }

    @Override
    public void deleteById(Long groupPostId) {
        jpaGroupPostRepository.deleteById(groupPostId);
    }


}
