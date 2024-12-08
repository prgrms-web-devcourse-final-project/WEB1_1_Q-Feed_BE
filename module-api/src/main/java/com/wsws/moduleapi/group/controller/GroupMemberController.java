package com.wsws.moduleapi.group.controller;

import com.wsws.moduleapi.group.dto.GroupMemberDetailApiResponse;
import com.wsws.moduleapplication.group.dto.GroupMemberDetailServiceResponse;
import com.wsws.moduleapplication.group.service.GroupMemberService;
import com.wsws.modulesecurity.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
@SecurityRequirement(name = "bearerAuth") // Security 적용
public class GroupMemberController {

    private final GroupMemberService groupMemberService;

    @PostMapping("/{groupId}/join")
    @Operation(summary = "그룹 가입", description = "현재 인증된 사용자로 그룹에 가입합니다.")
    public ResponseEntity<String> joinGroup(
            @Parameter(description = "가입할 그룹 ID") @PathVariable Long groupId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String userId = userPrincipal.getId();
        groupMemberService.joinGroup(groupId, userId);
        return ResponseEntity.ok("그룹에 가입되었습니다.");
    }

    @DeleteMapping("/{groupId}/leave")
    @Operation(summary = "그룹 탈퇴", description = "현재 인증된 사용자로 그룹을 탈퇴합니다.")
    public ResponseEntity<String> leaveGroup(
            @Parameter(description = "탈퇴할 그룹 ID") @PathVariable Long groupId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String userId = userPrincipal.getId();
        groupMemberService.leaveGroup(groupId, userId);
        return ResponseEntity.ok("그룹을 탈퇴하였습니다.");
    }

    @GetMapping("/{groupId}/members")
    @Operation(summary = "그룹 내 멤버 조회", description = "해당 그룹의 내의 모든 멤버들을 조회합니다.")
    public ResponseEntity<List<GroupMemberDetailApiResponse>> getGroupMembers(
            @Parameter(description = "모든 멤버들 조회할 그룹 ID") @PathVariable Long groupId) {

        List<GroupMemberDetailServiceResponse> members = groupMemberService.getGroupMembers(groupId);

        List<GroupMemberDetailApiResponse> apiResponses = members.stream()
                .map(GroupMemberDetailApiResponse::new)
                .toList();
        return ResponseEntity.ok(apiResponses);
    }

    @DeleteMapping("/{groupId}/members/{memberId}")
    @Operation(summary = "멤버 강제 퇴장", description = "해당 그룹 내의 멤버를 강제 퇴장 처리합니다.")
    public ResponseEntity<String> forceRemoveMember(
            @Parameter(description = "해당 멤버가 속해있는 그룹 ID") @PathVariable Long groupId,
            @Parameter(description = "강제 퇴장시킬 멤버의 ID") @PathVariable Long memberId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        String adminId = userPrincipal.getId();
        groupMemberService.forceRemoveMember(groupId, adminId, memberId);

        return ResponseEntity.ok("멤버가 탈퇴되었습니다.");
    }


}
