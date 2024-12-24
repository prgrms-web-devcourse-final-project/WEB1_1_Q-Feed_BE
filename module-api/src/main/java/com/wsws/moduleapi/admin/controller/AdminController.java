package com.wsws.moduleapi.admin.controller;

import com.wsws.moduleapplication.admin.service.AdminService;
import com.wsws.moduleapplication.report.dto.ReportDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    //신고 목록 조회
    @GetMapping("/report")
    public ResponseEntity<?> getAllReports(){
        List<ReportDetails> reportList = adminService.getReportList();
        return ResponseEntity.ok(reportList);
    }

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

    // 사용자 누적 신고 횟수 조회
    @GetMapping("/users/{userId}/count")
    public ResponseEntity<Long> getUserCount(@PathVariable String userId){
        Long reportCount = adminService.getReportCount(userId);
        return ResponseEntity.ok(reportCount);
    }
}
