package com.wsws.moduleapplication.report.dto;

import java.time.LocalDateTime;

public record ReportDetails(
        Long id,
        String reportedUserId,
        String reporterId,
        String reason,
        LocalDateTime reportedAt
) {
}
