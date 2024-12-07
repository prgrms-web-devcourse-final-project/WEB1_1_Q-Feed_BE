package com.wsws.moduleapi.group.controller;

import com.wsws.moduleapplication.group.dto.CreateGroupCommentRequest;
import com.wsws.moduleapplication.group.service.GroupCommentService;
import com.wsws.modulesecurity.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupCommentController {

    private final GroupCommentService groupcommentService;

    // 그룹 게시글 댓글 작성
    @PostMapping("/posts/{groupPostId}/comments")
    @Operation(summary = "그룹 게시글 댓글 추가", description = "특정 게시글에 댓글을 추가합니다.")
    public ResponseEntity<String> createGroupComment(
            @RequestBody CreateGroupCommentRequest request,
            @PathVariable Long groupPostId,
            @AuthenticationPrincipal UserPrincipal userPrincipal){
        String userId = userPrincipal.getId();
        groupcommentService.createGroupComment( request, groupPostId, userId);
        return ResponseEntity.status(201).body("그룹 게시글 댓글이 생성되었습니다.");
    }







    // 그룹 댓글 좋아요
//    @PostMapping("/comments/{group-comment-id}/likes")

    // 그룹 게시글 댓글 삭제
//    @DeleteMapping("/comments/{group-comment-id}")

}
