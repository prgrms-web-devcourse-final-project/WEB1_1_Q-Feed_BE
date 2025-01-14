package com.wsws.moduleapplication.report.service;

import com.wsws.moduleapplication.report.dto.CreateReportRequest;
import com.wsws.moduleapplication.report.dto.ReportDetails;
import com.wsws.moduledomain.admincontext.report.aggregate.Report;
import com.wsws.moduledomain.admincontext.report.repo.ReportRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportService reportService;

    @Nested
    @DisplayName("createReport 메서드는")
    class DescribeCreateReport {

        @Test
        @DisplayName("CreateReportRequest를 받아서 신고를 생성할 수 있다.")
        void itCreatesReport() {
            // given
            String reportedUserId = "reportedUserId";
            String reporterId = "reporterId";
            String reason = "spam";
            CreateReportRequest reportRequest = new CreateReportRequest(reportedUserId, reporterId, reason);

            // when
            reportService.createReport(reportRequest);

            // then
            verify(reportRepository, times(1)).save(any(Report.class));
        }
    }

    @Nested
    @DisplayName("getReportsByReportedUserId 메서드는")
    class DescribeGetReportsByReportedUserId {

        @Test
        @DisplayName("특정 사용자의 신고 이력을 반환한다.")
        void itReturnsReportsForSpecificUser() {
            // given
            String reportedUserId = "reportedUserId";
            Report report1 = Report.of(1L, reportedUserId, "reporterId1", "spam", LocalDateTime.now());
            Report report2 = Report.of(2L, reportedUserId, "reporterId2", "abuse", LocalDateTime.now());
            when(reportRepository.findByReportedUserId(reportedUserId)).thenReturn(List.of(report1, report2));

            // when
            List<ReportDetails> reports = reportService.getReportsByReportedUserId(reportedUserId);

            // then
            assertThat(reports).hasSize(2);
            assertThat(reports).extracting("reportedUserId").containsOnly(reportedUserId);
            assertThat(reports).extracting("reason").contains("spam", "abuse");

            verify(reportRepository, times(1)).findByReportedUserId(reportedUserId);
        }

        @Test
        @DisplayName("신고 이력이 없으면 빈 리스트를 반환한다.")
        void itReturnsEmptyListWhenNoReportsFound() {
            // given
            String reportedUserId = "unknownUser";
            when(reportRepository.findByReportedUserId(reportedUserId)).thenReturn(List.of());

            // when
            List<ReportDetails> reports = reportService.getReportsByReportedUserId(reportedUserId);

            // then
            assertThat(reports).isEmpty();

            verify(reportRepository, times(1)).findByReportedUserId(reportedUserId);
        }
    }
}