package com.wsws.moduleinfra.report.entity;

import com.wsws.moduledomain.admincontext.report.aggregate.Report;
import org.springframework.stereotype.Component;

@Component
public class ReportEntityMapper {

    public static Report toDomain(ReportEntity reportEntity) {
        return Report.of(
                reportEntity.getId(),
                reportEntity.getReportedUserId(),
                reportEntity.getReporterUserId(),
                reportEntity.getReason(),
                reportEntity.getReportedAt()
        );
    }

    public static ReportEntity toEntity(Report report) {
        ReportEntity entity = new ReportEntity(
                report.getReportedUserId(),
                report.getReporterUserId(),
                report.getReason(),
                report.getReportedAt()
        );

        return entity;

    }
}
