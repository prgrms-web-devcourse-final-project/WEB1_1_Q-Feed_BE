package com.wsws.moduleapplication.group.service;

import com.wsws.moduleapplication.group.dto.GroupMemberDetailServiceResponse;
import com.wsws.moduledomain.group.Group;
import com.wsws.moduledomain.group.GroupMember;
import com.wsws.moduledomain.group.dto.GroupMemberDetailDto;
import com.wsws.moduledomain.group.repo.GroupMemberRepository;
import com.wsws.moduledomain.group.repo.GroupRepository;
import com.wsws.moduledomain.group.vo.GroupId;
import com.wsws.moduledomain.user.repo.UserRepository;
import com.wsws.moduledomain.user.vo.UserId;
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
//        //그룹 멤버 확인
//        validateGroupMembers(groupMember, groupId);

        groupMemberRepository.deleteById(groupMember.getGroupMemberId());
    }


    private Group findGroupById(Long groupId) {
        return groupRepository.findById(GroupId.of(groupId))
                .orElseThrow(() -> new IllegalArgumentException("그룹을 찾을 수 없습니다."));
    }

    private void validateAdminPermission(Group group, String adminId) {
        if (!group.getAdminId().equals(UserId.of(adminId))) {
            throw new IllegalStateException("권한이 없습니다. 그룹 관리자만 멤버를 강제 퇴장시킬 수 있습니다.");
        }
    }

    private GroupMember findMemberById(Long memberId) {
        return groupMemberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));
    }

    private void validateGroupMembers(GroupMember groupMember, Long memberId) {
        if (!groupMember.getGroupMemberId().equals(memberId)) {
            throw new IllegalArgumentException("해당 멤버는 이 그룹에 속해 있지 않습니다.");
        }
    }
}
