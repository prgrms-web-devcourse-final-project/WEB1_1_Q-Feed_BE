package com.wsws.moduledomain.admincontext.report.repo;

import com.wsws.moduledomain.admincontext.report.aggregate.Report;

import java.util.List;

public interface ReportRepository {
    void save(Report report);
    List<Report> findByReportedUserId(String userId);
    List<Report> findAllReports();
    Long countByReportedUserId(String userId);
}
