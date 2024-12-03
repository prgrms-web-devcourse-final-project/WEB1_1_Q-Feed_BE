package com.wsws.moduleapplication.group.service;

import com.wsws.moduleapplication.group.dto.GroupMemberDetailServiceResponse;
import com.wsws.moduledomain.group.Group;
import com.wsws.moduledomain.group.GroupMember;
import com.wsws.moduledomain.group.dto.GroupMemberDetailDto;
import com.wsws.moduledomain.group.repo.GroupMemberRepository;
import com.wsws.moduledomain.group.repo.GroupRepository;
import com.wsws.moduledomain.group.vo.GroupId;
import com.wsws.moduledomain.user.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;
    private final GroupRepository groupRepository;

    @Transactional
    public void joinGroup(Long groupId, String userId) {
        Group group = findGroupById(groupId);

        // 이미 해당 그룹에 멤버가 있는지 확인
        if (groupMemberRepository.existsByUserIdAndGroupId(userId, groupId)) {
            throw new IllegalStateException("이미 그룹에 가입되어 있습니다.");
        }
        GroupMember groupMember = GroupMember.create(null,userId, groupId);
        groupMemberRepository.save(groupMember);
    }

    @Transactional
    public void leaveGroup(Long groupId, String userId) {
        Group group = findGroupById(groupId);

        // 그룹에서 해당 멤버 찾기
        GroupMember groupMember = groupMemberRepository.findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new IllegalArgumentException("그룹에서 해당 멤버를 찾을 수 없습니다."));

        groupMemberRepository.delete(groupMember);
    }

    public List<GroupMemberDetailServiceResponse> getGroupMembers(Long groupId) {
        findGroupById(groupId);
        // 그룹 멤버 조회
        List<GroupMemberDetailDto> groupMembers = groupMemberRepository.findMembersByGroupId(groupId);

        //domaindto->serviceresponse
        return groupMembers.stream()
                .map(GroupMemberDetailServiceResponse::new)
                .collect(Collectors.toList());
    }


    private Group findGroupById(Long groupId) {
        return groupRepository.findById(GroupId.of(groupId))
                .orElseThrow(() -> new IllegalArgumentException("그룹을 찾을 수 없습니다."));
    }
}
