package com.wsws.moduleapi.group.controller;

import com.wsws.moduleapi.auth.dto.AuthResponse;
import com.wsws.moduleapi.group.dto.GroupApiResponse;
import com.wsws.moduleapi.group.dto.GroupDetailApiResponse;
import com.wsws.moduleapi.group.dto.GroupResponse;
import com.wsws.moduleapplication.group.dto.CreateGroupRequest;
import com.wsws.moduleapplication.group.dto.GroupDetailServiceResponse;
import com.wsws.moduleapplication.group.dto.GroupServiceResponse;
import com.wsws.moduleapplication.group.dto.UpdateGroupRequest;
import com.wsws.moduleapplication.group.service.GroupService;
import com.wsws.modulesecurity.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
@SecurityRequirement(name = "bearerAuth") // Security 적용

public class GroupController {

    private final GroupService groupService;

    //그룹 생성
    @PostMapping("/create")
    @Operation(summary = "그룹 생성", description = "원하는 카테고리에 그룹을 생성합니다.")
    public ResponseEntity<GroupResponse> createGroup(@RequestBody CreateGroupRequest req, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String adminId = userPrincipal.getId();
        groupService.createGroup(req, adminId);
        return ResponseEntity.status(201).body(new GroupResponse("그룹 생성이 완료되었습니다."));
    }

    //그룹 수정
    @PatchMapping("/{groupId}")
    @Operation(summary = "그룹 수정", description = "그룹의 정보를 수정합니다.")
    public ResponseEntity<GroupResponse> updateGroup(
            @Parameter(description = "수정할 그룹 ID") @PathVariable Long groupId,
            @RequestBody UpdateGroupRequest req,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String adminId = userPrincipal.getId();
        groupService.updateGroup(groupId, req, adminId);
        return ResponseEntity.status(200).body(new GroupResponse("그룹 수정이 완료되었습니다."));
    }

    //그룹 삭제
    @DeleteMapping("/{groupId}")
    @Operation(summary = "그룹 삭제", description = "그룹을 삭제합니다.")
    public ResponseEntity<GroupResponse> deleteGroup(
            @Parameter(description = "삭제할 그룹 ID")@PathVariable Long groupId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String adminId = userPrincipal.getId();
        groupService.deleteGroup(groupId, adminId);
        return ResponseEntity.status(200).body(new GroupResponse("그룹 삭제가 완료되었습니다."));
    }

    //그룹 상태 변경
    @PatchMapping("/{groupId}/status")
    @Operation(summary = "그룹 상태 변경", description = "그룹의 상태를 모집완료/모집중으로 변경합니다.")
    public ResponseEntity<GroupResponse> ChangeGroupStatus(
            @Parameter(description = "상태를 변경할 그룹 ID") @PathVariable Long groupId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String adminId = userPrincipal.getId();
        groupService.ChangeGroupStatus(groupId, adminId);
        return ResponseEntity.status(200).body(new GroupResponse("그룹 상태가 변경되었습니다."));
    }

    //카테고리로 그룹 목록 조회
    @GetMapping("/{categoryId}")
    @Operation(summary = "그룹 목록 조회", description = "원하는 카테고리의 그룹 목록들을 조회합니다.")
    public ResponseEntity<List<GroupApiResponse>> getGroupsByCategory(
            @Parameter(description = "그룹 목록을 조회할 카테고리 ID") @PathVariable Long categoryId,
            @Parameter(description = "커서로 사용할 마지막 팔로워의 시간", example = "2024-01-01T00:00:00") @RequestParam(required = false) String cursor,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size) {
        LocalDateTime parsedCursor;

        // cursor가 null이거나 잘못된 형식일 경우 기본 값 처리
        try {
            parsedCursor = cursor != null ? LocalDateTime.parse(cursor) : LocalDateTime.now();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid cursor format. Please use the correct ISO format (e.g. 2024-01-01T00:00:00)");
        }

        List<GroupServiceResponse> groups = groupService.getGroupsByCategory(categoryId, parsedCursor, size);
        //변환작업
        List<GroupApiResponse> apiResponses = groups.stream()
                .map(GroupApiResponse::new)
                .toList();
        return ResponseEntity.ok(apiResponses);
    }

    //그룹 상세 조회
    @GetMapping("/{groupId}/detail")
    @Operation(summary = "그룹 상세 조회", description = "해당 그룹을 상세 조회합니다.")
    public ResponseEntity<GroupDetailApiResponse> getGroupDetail(
            @Parameter(description = "상세 조회할 그룹 ID") @PathVariable Long groupId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String userId = userPrincipal.getId();
        GroupDetailServiceResponse response = groupService.getGroupDetail(groupId,userId);
        //변환작업
        GroupDetailApiResponse apiResponse = new GroupDetailApiResponse(response);
        return ResponseEntity.ok(apiResponse);
    }


}
