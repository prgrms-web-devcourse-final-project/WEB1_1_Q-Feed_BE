package com.wsws.moduleapi.report.dto;

public record ReportRequest(
        String reportedUserId,
        String reason
) {

}
