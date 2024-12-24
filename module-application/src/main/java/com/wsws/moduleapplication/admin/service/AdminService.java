package com.wsws.moduleapplication.admin.service;

import com.wsws.moduleapplication.report.dto.ReportDetails;
import com.wsws.moduleapplication.usercontext.user.exception.UserNotFoundException;
import com.wsws.moduledomain.admincontext.report.aggregate.Report;
import com.wsws.moduledomain.admincontext.report.repo.ReportRepository;
import com.wsws.moduledomain.usercontext.user.aggregate.User;
import com.wsws.moduledomain.usercontext.user.repo.UserRepository;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final UserRepository userRepository;
    private final ReportRepository reportRepository;

    //신고 목록 관리자 조회
    @Transactional(readOnly = true)
    public List<ReportDetails> getReportList(){
        List<Report> reports = reportRepository.findAllReports();
        return reports.stream()
                .map(report -> new ReportDetails(
                        report.getId(),
                        report.getReportedUserId(),
                        report.getReporterUserId(),
                        report.getReason(),
                        report.getReportedAt()
                )).toList();
    }

    //사용자 계정 비활성화
    public void deactivateUser(String userId){
        User user = findUserByIdOrThrow(userId);
        user.deactivate();

        userRepository.save(user);
    }

    //사용자 계정 활성화
    public void activateUser(String userId){
        User user = findUserByIdOrThrow(userId);
        user.activate();

        userRepository.save(user);
    }

    //사용자 누적 신고 횟수 조회
    public Long getReportCount(String ReportedUserId){
        User user = findUserByIdOrThrow(ReportedUserId);

        Long count = reportRepository.countByReportedUserId(user.getId().getValue());

        return count;

    }




    private User findUserByIdOrThrow(String userId) {
        return userRepository.findById(UserId.of(userId))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
    }


}
