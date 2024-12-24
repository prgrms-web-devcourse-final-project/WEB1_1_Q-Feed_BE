package com.wsws.moduleinfra.report.repo;

import com.wsws.moduledomain.admincontext.report.aggregate.Report;
import com.wsws.moduleinfra.report.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface JpaReportRepository extends JpaRepository<ReportEntity, Long> {
    List<ReportEntity> findByReportedUserId(String reportedUserId);
    List<ReportEntity> findAllReport();
    Long countByReportedUserId(String reportedUserId);

}
