package com.wsws.moduleapi.group.controller;

import com.wsws.moduleapi.group.dto.GroupPostApiResponse;
import com.wsws.moduleapplication.group.dto.CreateGroupPostRequest;
import com.wsws.moduleapplication.group.dto.GroupPostServiceResponse;
import com.wsws.moduleapplication.group.service.GroupPostService;
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

    // 게시글 상세 조회
//    @GetMapping("/posts/{groupPostId}")
//    public ResponseEntity<GroupPostDetailApiResponse> getGroupPostDetail(@PathVariable Long groupPostId){
//        GroupPostDetailServiceResponse response = groupPostService.
//
//        return ResponseEntity.status(200).body(groupPostService.postDetailView(groupPostId));
//    }


    // 게시글 목록 조회
    @GetMapping("/{groupId}/posts")
    @Operation(summary = "게시글 목록 조회", description = "해당 그룹의 게시글을 조회합니다.")

    public ResponseEntity<List<GroupPostApiResponse>> getPosts(@PathVariable Long groupId){
        List<GroupPostServiceResponse> posts = groupPostService.getGroupPostsList(groupId);
        List<GroupPostApiResponse> apiResponses = posts.stream()
                .map(GroupPostApiResponse::new)
                .toList();
        return ResponseEntity.ok(apiResponses);
    }


    // 게시글 생성
    @PostMapping("/{groupId}/posts")
    @Operation(summary = "게시글 생성", description = "해당 그룹의 게시글을 작성합니다.")
    public ResponseEntity<String> createGroupPost(
            @RequestBody CreateGroupPostRequest request,
            @PathVariable Long groupId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String userId = userPrincipal.getId();
        groupPostService.createGroupPost(request,groupId, userId);
        return ResponseEntity.status(200).body("그룹 게시글이 생성되었습니다");
    }

    // 게시글 삭제
    @DeleteMapping("/posts/{groupPostId}")
    @Operation(summary = "게시글 삭제", description = "특정 게시글을 삭제합니다.")
    public ResponseEntity<String> deleteGroupPost(
            @PathVariable Long groupPostId,@AuthenticationPrincipal UserPrincipal userPrincipal){
            String userId = userPrincipal.getId();
        groupPostService.deleteGroupPost(groupPostId, userId);
        return ResponseEntity.status(200).body("그룹 게시글이 삭제되었습니다.");
    }
}
