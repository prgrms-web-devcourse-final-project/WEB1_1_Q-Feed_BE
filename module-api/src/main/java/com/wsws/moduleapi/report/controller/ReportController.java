package com.wsws.moduleapi.report.controller;

import com.wsws.moduleapi.report.dto.ReportMapper;
import com.wsws.moduleapi.report.dto.ReportRequest;
import com.wsws.moduleapplication.report.dto.CreateReportRequest;
import com.wsws.moduleapplication.report.dto.ReportDetails;
import com.wsws.moduleapplication.report.service.ReportService;
import com.wsws.modulesecurity.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final ReportMapper reportMapper;

    //신고 생성
    @PostMapping
    @Operation(summary = "신고 생성", description = "특정 사용자를 신고합니다")
    public ResponseEntity<Void> report(@RequestBody ReportRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String userId = userPrincipal.getId();
        CreateReportRequest serviceRequest = reportMapper.toCreateReportRequest(request, userId);
        reportService.createReport(serviceRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //사용자 신고 조회
    @Operation(summary = "사용자 신고 내역 조회", description = "해당 사용자의 신고 내열을 조회합니다.")
    @GetMapping("/{userId}")
    public ResponseEntity<List<ReportDetails>> getUserReports(@PathVariable String userId){
        List<ReportDetails> reports = reportService.getReportsByReportedUserId(userId);
        return ResponseEntity.ok(reports);
    }
}
