package com.wsws.moduleapi.admin.controller;

import com.wsws.moduleapplication.admin.service.AdminService;
import com.wsws.moduleapplication.report.dto.ReportDetails;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "신고 목록 조회", description = "모든 신고 목록을 조회합니다.")
    @GetMapping("/report")
    public ResponseEntity<?> getAllReports(){
        List<ReportDetails> reportList = adminService.getReportList();
        return ResponseEntity.ok(reportList);
    }

    // 사용자 비활성화
    @Operation(summary = "사용자 비활성화", description = "해당 사용자의 계정을 비활성화합니다.")
    @PatchMapping("/users/{userId}/deactive")
    public ResponseEntity<?> deactiveUser(@PathVariable String userId){
        adminService.deactivateUser(userId);
        return ResponseEntity.ok().build();
    }

    //사용자 활성화
    @Operation(summary = "사용자 활성화", description = "해당 사용자의 계정을 활성화합니다.")
    @PatchMapping("/users/{userId}/active")
    public ResponseEntity<?> activeUser(@PathVariable String userId){
        adminService.activateUser(userId);
        return ResponseEntity.ok().build();
    }

    // 사용자 누적 신고 횟수 조회
    @Operation(summary = "사용자 신고 누적 횟수 조회", description = "해당 사용자의 신고 누적 횟수를 조회합니다.")
    @GetMapping("/users/{userId}/count")
    public ResponseEntity<Long> getUserCount(@PathVariable String userId){
        Long reportCount = adminService.getReportCount(userId);
        return ResponseEntity.ok(reportCount);
    }
}
