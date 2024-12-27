package com.wsws.moduleapplication.report.service;

import com.wsws.moduleapplication.report.dto.CreateReportRequest;
import com.wsws.moduleapplication.report.dto.ReportDetails;
import com.wsws.moduledomain.admincontext.report.aggregate.Report;
import com.wsws.moduledomain.admincontext.report.repo.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    // 신고 생성
    public void createReport(CreateReportRequest request){
        Report report = Report.create(request.reportedUserId(), request.reporterId(), request.reason());
        reportRepository.save(report);

    }

    //특정 사용자 신고 이력 조회
    public List<ReportDetails> getReportsByReportedUserId(String reportedUserId) {
        List<Report> reports = reportRepository.findByReportedUserId(reportedUserId);
        return reports.stream()
                .map(report -> new ReportDetails(
                        report.getId(),
                        report.getReportedUserId(),
                        report.getReporterUserId(),
                        report.getReason(),
                        report.getReportedAt()
                )).toList();
    }



}
