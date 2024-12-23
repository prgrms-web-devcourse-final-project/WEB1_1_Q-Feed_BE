package com.wsws.moduleapi.report.dto;

public record ReportRequest(
        String reporterId,
        String reportedUserId,
        String reason
) {

}
