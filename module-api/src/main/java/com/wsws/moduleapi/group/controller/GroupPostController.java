package com.wsws.moduleapi.group.controller;

import com.wsws.moduleapi.group.dto.GroupPostApiResponse;
import com.wsws.moduleapplication.group.dto.CreateGroupPostRequest;
import com.wsws.moduleapplication.group.dto.GroupPostServiceResponse;
import com.wsws.moduleapplication.group.service.GroupPostService;
import com.wsws.moduleapplication.user.dto.LikeServiceRequest;
import com.wsws.modulesecurity.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
        List<GroupPostServiceResponse> posts = groupPostService.getGroupPostsList(groupId);
        List<GroupPostApiResponse> apiResponses = posts.stream()
                .map(GroupPostApiResponse::new)
                .toList();
        return ResponseEntity.ok(apiResponses);
    }

    @PostMapping("/{groupId}/posts")
    @Operation(summary = "게시글 생성", description = "특정 그룹에 새로운 게시글을 작성합니다.")
    public ResponseEntity<String> createGroupPost(
            @RequestBody CreateGroupPostRequest request,
            @PathVariable Long groupId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return ResponseEntity.status(401).body("인증되지 않은 사용자입니다.");
        }

        String userId = userPrincipal.getId();
        groupPostService.createGroupPost(request, groupId, userId);
        return ResponseEntity.status(201).body("그룹 게시글이 생성되었습니다.");
    }

    @DeleteMapping("/posts/{groupPostId}")
    @Operation(summary = "게시글 삭제", description = "특정 게시글을 삭제합니다.")
    public ResponseEntity<String> deleteGroupPost(
            @PathVariable Long groupPostId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return ResponseEntity.status(401).body("인증되지 않은 사용자입니다.");
        }

        String userId = userPrincipal.getId();
        groupPostService.deleteGroupPost(groupPostId, userId);
        return ResponseEntity.ok("그룹 게시글이 삭제되었습니다.");
    }

    @PostMapping("/posts/{groupPostId}/likes")
    @Operation(summary = "게시글 좋아요 추가", description = "현재 인증된 사용자로 특정 게시글에 좋아요를 추가합니다.")
    public ResponseEntity<String> likeToPost(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Parameter(description = "좋아요를 추가할 게시글 ID") @PathVariable("groupPostId") Long groupPostId) {

        if (userPrincipal == null) {
            return ResponseEntity.status(401).body("인증되지 않은 사용자입니다.");
        }

        String userId = userPrincipal.getId();
        LikeServiceRequest request = new LikeServiceRequest(userId, "GROUP_POST", groupPostId);
        groupPostService.addLikeToGroupPost(request);

        return ResponseEntity.ok("그룹 게시글에 좋아요가 추가되었습니다.");
    }

    @PostMapping("/posts/{groupPostId}/cancel-likes")
    @Operation(summary = "게시글 좋아요 취소", description = "현재 인증된 사용자로 특정 게시글의 좋아요를 취소합니다.")
    public ResponseEntity<String> cancelLikeToPost(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Parameter(description = "좋아요를 취소할 게시글 ID") @PathVariable("groupPostId") Long groupPostId) {

        if (userPrincipal == null) {
            return ResponseEntity.status(401).body("인증되지 않은 사용자입니다.");
        }

        String userId = userPrincipal.getId();
        LikeServiceRequest request = new LikeServiceRequest(userId, "GROUP_POST", groupPostId);
        groupPostService.cancelLikeToGroupPost(request);

        return ResponseEntity.ok("그룹 게시글에 좋아요가 취소되었습니다.");
    }
}
