package com.wsws.moduleapi.group.controller;

import com.wsws.moduleapi.group.dto.GroupMemberDetailApiResponse;
import com.wsws.moduleapplication.group.dto.GroupMemberDetailServiceResponse;
import com.wsws.moduleapplication.group.service.GroupMemberService;
import com.wsws.modulesecurity.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupMemberController {

    private final GroupMemberService groupMemberService;

    @PostMapping("/{groupId}/join")
    public ResponseEntity<String> joinGroup(@PathVariable Long groupId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String userId = userPrincipal.getId();
        groupMemberService.joinGroup(groupId, userId);
        return ResponseEntity.ok("그룹에 가입되었습니다.");
    }

    @PostMapping("/{groupId}/leave")
    public ResponseEntity<String> leaveGroup(@PathVariable Long groupId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String userId = userPrincipal.getId();
        groupMemberService.leaveGroup(groupId, userId);
        return ResponseEntity.ok("그룹에서 탈퇴되었습니다.");
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<GroupMemberDetailApiResponse>> getGroupMembers(@PathVariable Long groupId) {
        // 서비스 호출
        List<GroupMemberDetailServiceResponse> members = groupMemberService.getGroupMembers(groupId);
        //변환작업
        List<GroupMemberDetailApiResponse> apiResponses = members.stream()
                .map(GroupMemberDetailApiResponse::new)
                .toList();
        return ResponseEntity.ok(apiResponses);
    }
}
