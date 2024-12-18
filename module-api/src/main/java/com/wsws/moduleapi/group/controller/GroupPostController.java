package com.wsws.moduleapi.group.controller;

import com.wsws.moduleapi.group.dto.GroupDetailApiResponse;
import com.wsws.moduleapi.group.dto.GroupPostApiResponse;
import com.wsws.moduleapi.group.dto.GroupPostDetailApiResponse;
import com.wsws.moduleapplication.group.dto.CreateGroupPostRequest;
import com.wsws.moduleapplication.group.dto.GroupPostDetailServiceResponse;
import com.wsws.moduleapplication.group.service.GroupPostService;
import com.wsws.moduleapplication.feed.dto.LikeServiceRequest;
import com.wsws.modulesecurity.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupPostController {

    private final GroupPostService groupPostService;

    @GetMapping("/{groupId}/posts")
    @Operation(summary = "게시글 목록 조회", description = "특정 그룹의 게시글 목록을 조회합니다.")
    public ResponseEntity<List<GroupPostApiResponse>> getPosts(@PathVariable Long groupId) {
        return ResponseEntity.ok(
                groupPostService.getGroupPostsList(groupId).stream()
                        .map(GroupPostApiResponse::new)
                        .toList()
        );
    }

    // 게시글 상세 조회
    @GetMapping("/posts/{groupPostId}")
    @Operation(summary = "그룹 게시물 상세 조회", description = "해당 그룹 게시물을 상세 조회합니다.")
    public ResponseEntity<GroupPostDetailApiResponse> getPostDetail(
            @PathVariable Long groupPostId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String userId = userPrincipal.getId();
        GroupPostDetailServiceResponse response = groupPostService.getGroupPostDetail(groupPostId,userId);
        GroupPostDetailApiResponse apiResponse = new GroupPostDetailApiResponse(response);
        return ResponseEntity.ok(apiResponse);
    }


    @PostMapping("/{groupId}/posts")
    @Operation(summary = "게시글 생성", description = "특정 그룹에 새로운 게시글을 작성합니다.")
    public ResponseEntity<String> createGroupPost(
            @RequestBody CreateGroupPostRequest request,
            @PathVariable Long groupId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return processAuthenticatedRequest(userPrincipal, userId -> {
            groupPostService.createGroupPost(request, groupId, userId);
            return ResponseEntity.status(201).body("그룹 게시글이 생성되었습니다.");
        });
    }

    @DeleteMapping("/posts/{groupPostId}")
    @Operation(summary = "게시글 삭제", description = "특정 게시글을 삭제합니다.")
    public ResponseEntity<String> deleteGroupPost(
            @PathVariable Long groupPostId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return processAuthenticatedRequest(userPrincipal, userId -> {
            groupPostService.deleteGroupPost(groupPostId);
            return ResponseEntity.ok("그룹 게시글이 삭제되었습니다.");
        });
    }

    @PostMapping("/posts/{groupPostId}/likes")
    @Operation(summary = "게시글 좋아요 추가", description = "현재 인증된 사용자로 특정 게시글에 좋아요를 추가합니다.")
    public ResponseEntity<String> likeToPost(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long groupPostId) {
        return processAuthenticatedRequest(userPrincipal, userId -> {
            groupPostService.addLikeToGroupPost(
                    new LikeServiceRequest(userId, "GROUP_POST", groupPostId));
            return ResponseEntity.ok("그룹 게시글에 좋아요가 추가되었습니다.");
        });
    }

    @PostMapping("/posts/{groupPostId}/cancel-likes")
    @Operation(summary = "게시글 좋아요 취소", description = "현재 인증된 사용자로 특정 게시글의 좋아요를 취소합니다.")
    public ResponseEntity<String> cancelLikeToPost(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long groupPostId) {
        return processAuthenticatedRequest(userPrincipal, userId -> {
            groupPostService.cancelLikeToGroupPost(
                    new LikeServiceRequest(userId, "GROUP_POST", groupPostId));
            return ResponseEntity.ok("그룹 게시글에 좋아요가 취소되었습니다.");
        });
    }

    private ResponseEntity<String> processAuthenticatedRequest(
            UserPrincipal userPrincipal, AuthenticatedAction action) {
        if (userPrincipal == null) {
            return ResponseEntity.status(401).body("인증되지 않은 사용자입니다.");
        }
        return action.execute(userPrincipal.getId());
    }

    @FunctionalInterface
    private interface AuthenticatedAction {
        ResponseEntity<String> execute(String userId);
    }


}


