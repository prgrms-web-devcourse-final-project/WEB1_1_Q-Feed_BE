package com.wsws.moduleapplication.group.service;

import com.wsws.moduleapplication.group.dto.CreateGroupRequest;
import com.wsws.moduleapplication.group.dto.GroupDetailServiceResponse;
import com.wsws.moduleapplication.group.dto.GroupServiceResponse;
import com.wsws.moduleapplication.group.dto.UpdateGroupRequest;
import com.wsws.moduleapplication.usercontext.user.exception.ProfileImageProcessingException;
import com.wsws.moduleapplication.util.ProfileImageValidator;
import com.wsws.modulecommon.service.FileStorageService;
import com.wsws.moduledomain.group.Group;
import com.wsws.moduledomain.group.GroupMember;
import com.wsws.moduledomain.group.dto.GroupDetailDto;
import com.wsws.moduledomain.group.dto.GroupDto;
import com.wsws.moduledomain.group.dto.GroupMemberDto;
import com.wsws.moduledomain.group.dto.GroupPostDto;
import com.wsws.moduledomain.group.repo.GroupMemberRepository;
import com.wsws.moduledomain.group.repo.GroupPostRepository;
import com.wsws.moduledomain.group.repo.GroupRepository;
import com.wsws.moduledomain.group.vo.GroupId;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupMemberRepository groupMemberRepository;
    private final GroupRepository groupRepository;
    private final GroupPostRepository groupPostRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public void createGroup(CreateGroupRequest req, String adminId){
        // 그룹 이미지 처리
        String groupImageUrl = processGroupImage(req.url());

        Group group = Group.create(
                null,
                req.groupName(),
                req.description(),
                LocalDateTime.now(),
                adminId,
                req.categoryId(),
                groupImageUrl,
                req.isOpen()
        );
        Group saveGroup = groupRepository.save(group);
        GroupMember groupMember = GroupMember.create(null,adminId, saveGroup.getGroupId().getValue());
        groupMemberRepository.save(groupMember);
    }

    @Transactional
    public void updateGroup(Long groupId,UpdateGroupRequest req, String adminId){
        Group group = findGroupById(groupId);

        validateAdminPermission(group, adminId);

        //그룹 이미지 처리
        String groupImageUrl = processGroupImage(req.image());
        if (groupImageUrl == null) {
            groupImageUrl = group.getUrl();
        }

        //그룹 정보 수정
        group.updateGroupInfro(req.groupName(),req.description(),groupImageUrl);
        try {
            groupRepository.edit(group);
        } catch (RuntimeException e) {
            throw new RuntimeException("그룹이 존재하지않습니다.");
        }
    }

    @Transactional
    public void deleteGroup(Long groupId, String adminId){
        Group group = findGroupById(groupId);
        validateAdminPermission(group, adminId);
        groupRepository.deleteById(groupId);
    }

    @Transactional
    public void ChangeGroupStatus(Long groupId, String adminId){
        Group group = findGroupById(groupId);
        validateAdminPermission(group, adminId);

        //(true -> false, false -> true)
        group.changeVisibility(!group.isOpen());
        groupRepository.changeStatus(group);
    }

    //그룹 목록 조회
    public List<GroupServiceResponse> getGroupsByCategory(Long categoryId,LocalDateTime cursor, int size){
        List<GroupDto> groups = groupRepository.findByCategoryIdWithMemberCount(categoryId, cursor, size);

        //domaindto->serviceresponse
        return groups.stream()
                .map(GroupServiceResponse::new)
                .collect(Collectors.toList());
    }

    //그룹 상세조회
    @Transactional(readOnly = true)
    public GroupDetailServiceResponse getGroupDetail(Long groupId,String userId) {

        GroupDetailDto groupDetailDto = groupRepository.findGroupWithCategory(groupId)
                .orElseThrow(() -> new IllegalArgumentException("그룹을 찾을 수 없습니다."));

        List<GroupMemberDto> groupMembers = groupRepository.findMembersByGroupId(groupId);

        List<GroupPostDto> posts = groupPostRepository.findByGroupId(groupId);

        //본인이 해당 그룹에 존재하는지 여부
        boolean isMember = groupMemberRepository.existsByUserIdAndGroupId(userId, groupId);

        //해당 그룹에 존재 할 때만 post 볼 수 있음
        List<GroupPostDto> groupPosts = isMember ? posts : Collections.emptyList();

        return new GroupDetailServiceResponse(groupDetailDto, groupMembers, groupPosts);
    }

    //내가 참여중인 그룹 목록 조회
    public List<GroupServiceResponse> getJoinedGroups(String userId){
        List<GroupDto> groups = groupRepository.findJoinedGroupsByUserId(userId);

        //domaindto->serviceresponse
        return groups.stream()
                .map(GroupServiceResponse::new)
                .collect(Collectors.toList());
    }


    private Group findGroupById(Long groupId) {
        return groupRepository.findById(GroupId.of(groupId))
                .orElseThrow(() -> new IllegalArgumentException("그룹을 찾을 수 없습니다."));
    }

    private void validateAdminPermission(Group group, String adminId) {
        if (!group.getAdminId().equals(UserId.of(adminId))) {
            throw new IllegalStateException("권한이 없습니다. 관리자만 수정할 수 있습니다.");
        }
    }

    //프로필 이미지 처리
    private String processGroupImage(MultipartFile groupImageFile) {
        if (groupImageFile != null && !groupImageFile.isEmpty()) {
            try {
                ProfileImageValidator.validate(groupImageFile);
                return fileStorageService.saveFile(groupImageFile);
            } catch (Exception e) {
                throw ProfileImageProcessingException.EXCEPTION;
            }
        }
        return null; // 이미지가 없는 경우
    }


}