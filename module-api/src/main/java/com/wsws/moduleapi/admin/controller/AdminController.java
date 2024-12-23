package com.wsws.moduleapi.admin.controller;

import com.wsws.moduleapplication.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    //신고 목록 조회
    @GetMapping("/report")
    public ResponseEntity<?> getAllReports(){

    }

    //신고받은 사용자 목록 조회



    //사용자 비활성화



    // 사용자 비활성화
    @PatchMapping("/users/{userId}/deactive")
    public ResponseEntity<?> deactiveUser(@PathVariable String userId){
        adminService.deactivateUser(userId);
        return ResponseEntity.ok().build();
    }

    //사용자 활성화
    @PatchMapping("/users/{userId}/active")
    public ResponseEntity<?> activeUser(@PathVariable String userId){
        adminService.activateUser(userId);
        return ResponseEntity.ok().build();
    }
}
