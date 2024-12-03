package com.wsws.moduleapi.group.controller;

import com.wsws.moduleapplication.chat.dto.ChatRoomServiceRequest;
import com.wsws.moduleapplication.group.dto.CreateGroupRequest;
import com.wsws.moduleapplication.group.dto.UpdateGroupRequest;
import com.wsws.moduleapplication.group.service.GroupService;
import com.wsws.modulesecurity.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    //그룹 생성
    @PostMapping("/create")
    public ResponseEntity<String> createGroup(@RequestBody CreateGroupRequest req, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String adminId = userPrincipal.getId();
        groupService.createGroup(req, adminId);
        return ResponseEntity.status(201).body("그룹 생성이 완료되었습니다.");
    }

    //그룹 수정
    @PatchMapping("/{groupId}")
    public ResponseEntity<String> updateGroup(@PathVariable Long groupId,@RequestBody UpdateGroupRequest req,
                                              @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String adminId = userPrincipal.getId();
        groupService.updateGroup(groupId, req, adminId);
        return ResponseEntity.status(200).body("그룹 수정이 완료되었습니다.");
    }

    //그룹 삭제
    @DeleteMapping("/{groupId}")
    public ResponseEntity<String> deleteGroup(@PathVariable Long groupId,  @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String adminId = userPrincipal.getId();
        groupService.deleteGroup(groupId, adminId);
        return ResponseEntity.status(200).body("그룹 삭제가 완료되었습니다.");
    }



}
