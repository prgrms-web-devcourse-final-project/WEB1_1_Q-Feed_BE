package com.wsws.moduleapplication.report.dto;

public record CreateReportRequest(
        String reporterId,
        String reportedUserId,
        String reason
) {
    // 정적 팩토리 메서드
    public static CreateReportRequest create(ReportRequest request, String reporterId) {
        return new CreateReportRequest(
                reporterId,
                request.reportedUserId(),
                request.reason()
        );
    }
}
