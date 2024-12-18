package com.wsws.moduleapi.group.controller;

import com.wsws.moduleapi.group.dto.GroupCommentApiResponse;
import com.wsws.moduleapplication.group.dto.CreateGroupCommentRequest;
import com.wsws.moduleapplication.group.service.GroupCommentService;
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
public class GroupCommentController {

    private final GroupCommentService groupCommentService;

    // 그룹 게시글 댓글 작성
    @PostMapping("/posts/{groupPostId}/comments")
    @Operation(summary = "그룹 게시글 댓글 추가", description = "특정 게시글에 댓글을 추가합니다.")
    public ResponseEntity<String> createGroupComment(
            @RequestBody CreateGroupCommentRequest request,
            @PathVariable Long groupPostId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String userId = validateAndGetUserId(userPrincipal);
        groupCommentService.createGroupComment(request, groupPostId, userId);
        return ResponseEntity.status(201).body("그룹 게시글 댓글이 생성되었습니다.");
    }

    // 그룹 게시글 댓글 목록 조회
    @GetMapping("/{groupPostId}/comments")
    @Operation(summary = "게시글 댓글 목록 조회", description = "특정 그룹의 게시글 댓글의 목록을 조회합니다.")
    public ResponseEntity<List<GroupCommentApiResponse>> getComments(@PathVariable Long groupPostId){
        return ResponseEntity.ok(
                groupCommentService.getGroupCommentList(groupPostId).stream()
                        .map(GroupCommentApiResponse::new)
                        .toList()
        );
    }

    // 그룹 게시글 댓글 삭제
    @DeleteMapping("/comments/{groupCommentId}")
    @Operation(summary = "그룹 게시글 댓글 삭제", description = "특정 댓글을 삭제합니다.")
    public ResponseEntity<String> deleteGroupComment(
            @PathVariable Long groupCommentId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String userId = validateAndGetUserId(userPrincipal);
        groupCommentService.deleteGroupComment(groupCommentId, userId);
        return ResponseEntity.ok("그룹 게시글 댓글이 삭제되었습니다.");
    }

    @PostMapping("/comments/{groupCommentId}/likes")
    @Operation(summary = "게시글 댓글 좋아요 추가", description = "현재 인증된 사용자로 특정 게시글 댓글에 좋아요를 추가합니다.")
    public ResponseEntity<String> likeToComment(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long groupCommentId) {
        if (userPrincipal == null) {
            return ResponseEntity.status(401).body("인증되지 않은 사용자입니다.");
        }
        String userId = userPrincipal.getId();
        groupCommentService.addLikeToGroupComment(
                new LikeServiceRequest(userId, "GROUP_COMMENT", groupCommentId));
        return ResponseEntity.ok("그룹 게시글 댓글에 좋아요가 추가되었습니다.");
    }


    @PostMapping("/comments/{groupCommentId}/cancel-likes")
    @Operation(summary = "게시글 댓글 좋아요 취소", description = "현재 인증된 사용자로 특정 게시글의 좋아요를 취소합니다.")
    public ResponseEntity<String> cancelLikeToComment(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long groupCommentId) {
        if (userPrincipal == null) {
            return ResponseEntity.status(401).body("인증되지 않은 사용자입니다.");
        }
        String userId = userPrincipal.getId();
        groupCommentService.cancelLikeToGroupComment(
                new LikeServiceRequest(userId, "GROUP_COMMENT", groupCommentId));
        return ResponseEntity.ok("그룹 게시글 댓글에 좋아요가 취소되었습니다.");
    }

    // 사용자 인증
    private String validateAndGetUserId(UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            throw new IllegalArgumentException("인증되지 않은 사용자입니다.");
        }
        return userPrincipal.getId();
    }

    }



