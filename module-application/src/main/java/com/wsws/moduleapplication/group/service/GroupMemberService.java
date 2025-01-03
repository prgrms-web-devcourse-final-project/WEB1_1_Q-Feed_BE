package com.wsws.moduleapplication.group.service;

import com.wsws.moduleapplication.group.dto.GroupMemberDetailServiceResponse;
import com.wsws.moduleapplication.group.exception.*;
import com.wsws.moduledomain.group.Group;
import com.wsws.moduledomain.group.GroupMember;
import com.wsws.moduledomain.group.dto.GroupMemberDetailDto;
import com.wsws.moduledomain.group.repo.GroupMemberRepository;
import com.wsws.moduledomain.group.repo.GroupRepository;
import com.wsws.moduledomain.group.vo.GroupId;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
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
        findGroupById(groupId);

        validateAlreadyInGroup(userId, groupId);

        GroupMember groupMember = GroupMember.create(null,userId, groupId);
        groupMemberRepository.save(groupMember);
    }

    @Transactional
    public void leaveGroup(Long groupId, String userId) {
        findGroupById(groupId);

        GroupMember groupMember = findByUserIdAndGroupId(userId, groupId);

        groupMemberRepository.deleteById(groupMember.getGroupMemberId());
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

    @Transactional
    public void forceRemoveMember(Long groupId, String adminId, Long memberId) {
        Group group = findGroupById(groupId) ;
        // 그룹 관리자 확인
        validateAdminPermission(group, adminId);

        GroupMember groupMember = findMemberById(memberId);

        groupMemberRepository.deleteById(groupMember.getGroupMemberId());
    }


    private Group findGroupById(Long groupId) {
        return groupRepository.findById(GroupId.of(groupId))
                .orElseThrow(() -> GroupNotFoundException.EXCEPTION);
    }

    private GroupMember findMemberById(Long memberId) {
        return groupMemberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }

    private GroupMember findByUserIdAndGroupId(String userId, Long groupId) {
        return groupMemberRepository.findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }

    private void validateAdminPermission(Group group, String adminId) {
        if (!group.getAdminId().equals(UserId.of(adminId))) {
            throw UnauthorizedAccessException.EXCEPTION;
        }
    }

    public void validateAlreadyInGroup(String userId, Long groupId) {
        if (groupMemberRepository.existsByUserIdAndGroupId(userId, groupId)) {
            throw AlreadyInGroupException.EXCEPTION;
        }
    }

    private void validateGroupMembers(GroupMember groupMember, Long memberId) {
        if (!groupMember.getGroupMemberId().equals(memberId)) {
            throw MemberNotInGroupException.EXCEPTION;
        }
    }
}
