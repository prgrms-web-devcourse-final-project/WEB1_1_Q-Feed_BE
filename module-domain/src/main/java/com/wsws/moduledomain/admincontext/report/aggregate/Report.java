package com.wsws.moduledomain.admincontext.report.aggregate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {

    private Long id;
    private String reportedUserId; // 신고받은 사용자 식별자
    private String reporterUserId; // 신고한 사용자 식별자
    private String reason; // 신고사유
    private LocalDateTime reportedAt; // 신고 시간

    public static Report create(String reportedUserId, String reporterUserId, String reason) {
        Report report = new Report();
        report.reportedUserId = reportedUserId;
        report.reporterUserId = reporterUserId;
        report.reason = reason;
        report.reportedAt = LocalDateTime.now();
        return report;
    }

    public static Report of(Long id, String reportedUserId, String reporterUserId, String reason, LocalDateTime reportedAt) {
        Report report = new Report();
        report.id = id;
        report.reportedUserId = reportedUserId;
        report.reporterUserId = reporterUserId;
        report.reason = reason;
        report.reportedAt = reportedAt;
        return report;
    }
}
