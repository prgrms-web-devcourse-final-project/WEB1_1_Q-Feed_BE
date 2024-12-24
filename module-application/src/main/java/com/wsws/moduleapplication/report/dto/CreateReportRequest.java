package com.wsws.moduleapplication.report.dto;

public record CreateReportRequest(
        String reporterId,
        String reportedUserId,
        String reason
) {

}
