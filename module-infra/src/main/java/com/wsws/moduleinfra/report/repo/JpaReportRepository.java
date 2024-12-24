package com.wsws.moduleinfra.report.repo;

import com.wsws.moduleinfra.report.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface JpaReportRepository extends JpaRepository<ReportEntity, Long> {
    List<ReportEntity> findByReportedUserId(String reportedUserId);
    List<ReportEntity> findAll();
    Long countByReportedUserId(String reportedUserId);

}
