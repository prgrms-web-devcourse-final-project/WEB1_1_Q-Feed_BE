package com.wsws.moduleapi.report.dto;

import com.wsws.moduleapplication.report.dto.CreateReportRequest;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {

    public CreateReportRequest toCreateReportRequest(ReportRequest request, String reporterId) {
        return new CreateReportRequest(
                reporterId,
                request.reportedUserId(),
                request.reason()
        );
    }
}