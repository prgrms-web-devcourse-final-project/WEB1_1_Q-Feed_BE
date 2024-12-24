package com.wsws.moduleinfra.report.repo;

import com.wsws.moduledomain.admincontext.report.aggregate.Report;
import com.wsws.moduledomain.admincontext.report.repo.ReportRepository;
import com.wsws.moduleinfra.report.entity.ReportEntity;
import com.wsws.moduleinfra.report.entity.ReportEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReportRepositoryImpl implements ReportRepository {

    private final JpaReportRepository jpaReportRepository;

    @Override
    public void save(Report report) {
        //report를
        ReportEntity reportEntity = ReportEntityMapper.toEntity(report);
        jpaReportRepository.save(reportEntity);
    }

    @Override
    public List<Report> findByReportedUserId(String userId) {
        List<ReportEntity> reports = jpaReportRepository.findByReportedUserId(userId);
        return reports.stream()
                .map(ReportEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<Report> findAllReports() {
        List<ReportEntity> reports = jpaReportRepository.findAll();

        return reports.stream()
                .map(ReportEntityMapper::toDomain)
                .toList();
    }

    @Override
    public Long countByReportedUserId(String userId) {
        Long count = jpaReportRepository.countByReportedUserId(userId);
        return count;
    }


}
