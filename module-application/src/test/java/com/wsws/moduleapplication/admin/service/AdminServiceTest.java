package com.wsws.moduleapplication.admin.service;

import com.wsws.moduleapplication.report.dto.ReportDetails;
import com.wsws.moduleapplication.usercontext.user.exception.UserNotFoundException;
import com.wsws.moduledomain.admincontext.report.aggregate.Report;
import com.wsws.moduledomain.admincontext.report.repo.ReportRepository;
import com.wsws.moduledomain.usercontext.user.aggregate.User;
import com.wsws.moduledomain.usercontext.user.repo.UserRepository;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private AdminService adminService;

    @Nested
    @DisplayName("getReportList 메서드는")
    class DescribeGetReportList {

        @Test
        @DisplayName("리포트 목록을 조회할 수 있다")
        void getReportList() {

            // given
            Report report1 = Report.create( "reportedUser1", "reporter1", "Spam");
            Report report2 = Report.create("reportedUser2", "reporter2", "Abuse");

            when(reportRepository.findAllReports()).thenReturn(List.of(report1, report2));

            //when
            List<ReportDetails> reportList = adminService.getReportList();

            //then
            assertThat(reportList).hasSize(2);
            assertThat(reportList.get(0).reporterId()).isEqualTo("reporter1");
            assertThat(reportList.get(1).reporterId()).isEqualTo("reporter2");
            assertThat(reportList.get(0).reportedUserId()).isEqualTo("reportedUser1");
            assertThat(reportList.get(1).reportedUserId()).isEqualTo("reportedUser2");
            assertThat(reportList.get(0).reason()).isEqualTo("Spam");
            assertThat(reportList.get(1).reason()).isEqualTo("Abuse");

            verify(reportRepository, times(1)).findAllReports();


        }
    }

    @Nested
    @DisplayName("deactiveUser 메서드는")
    class DescribeDeactiveUser {

        @Test
        @DisplayName("userId를 받아 사용자 계정을 비활성화 할 수 있다.")
        void itDeactiveUser() {

            //given
            String userId = "user1";
            User user = mock(User.class);

            when(userRepository.findById(UserId.of(userId))).thenReturn(Optional.of(user));

            //when
            adminService.deactivateUser(userId);

            //then
            verify(user,times(1)).deactivate();
            verify(userRepository, times(1)).save(user);
        }

        @Test
        @DisplayName("userId가 존재하지 않으면 UserNotFoudnException을 던진다")
        void itThrowsUserNotFoudnException() {
            //given
            String userId = "user1";

            when(userRepository.findById(UserId.of(userId))).thenReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> adminService.deactivateUser(userId))
                    .isInstanceOf(UserNotFoundException.class);

            verify(userRepository, never()).save(any(User.class));


        }
    }

    @Nested
    @DisplayName("activateUser 메서드는")
    class DescribeActivateUser {

        @Test
        @DisplayName("userId를 받아서 계정을 활성화 할 수 있다.")
        void itActivateUser() {
            //given
            String userId = "user1";
            User user = mock(User.class);

            when(userRepository.findById(UserId.of(userId))).thenReturn(Optional.of(user));

            //when
            adminService.activateUser(userId);

            //then
            verify(user,times(1)).activate();
            verify(userRepository, times(1)).save(user);

        }

        @Test
        @DisplayName("userId가 존재하지 않으면 UserNotFoundException을 던진다")
        void itThrowsUserNotFoundExceptionIfUserNotFound() {
            // given
            String userId = "user1";

            when(userRepository.findById(UserId.of(userId))).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> adminService.activateUser(userId))
                    .isInstanceOf(UserNotFoundException.class);

            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("getReportCount 메서드는")
    class DescribeGetReportCount {

        @Test
        @DisplayName("신고된 사용자 userId를 받아서 누적 신고 횟수를 반환할 수 있다.")
        void itGetReportCount() {
            //given
            String reportedUserId = "reportedUser1";
            User user = mock(User.class);

            when(userRepository.findById(UserId.of(reportedUserId))).thenReturn(Optional.of(user));
            when(user.getId()).thenReturn(UserId.of(reportedUserId));
            when(reportRepository.countByReportedUserId(reportedUserId)).thenReturn(3L);

            //when
            Long reportCount = adminService.getReportCount(reportedUserId);

            //then
            assertThat(reportCount).isEqualTo(3L);

            verify(reportRepository, times(1)).countByReportedUserId(reportedUserId);

        }

        @Test
        @DisplayName("신고된 사용자 userId가 없다면 UserNotFoundException을 던진다.")
        void itThrowsUserNotFoundExceptionIfUserNotFound() {

            //given
            String reportedUserId = "reportedUser1";

            when(userRepository.findById(UserId.of(reportedUserId))).thenReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> adminService.getReportCount(reportedUserId)).isInstanceOf(UserNotFoundException.class);
            verify(reportRepository, never()).countByReportedUserId(reportedUserId);
        }
    }




}