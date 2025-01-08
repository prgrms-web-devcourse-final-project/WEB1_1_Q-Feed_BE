package com.wsws.moduledomain.admincontext.report.aggregate;

import com.wsws.moduledomain.admincontext.report.exception.InvalidReportTargetException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReportTest {

    @Test
    void create_메소드로_Report_객체를_생성할_수_있다(){
        //given
        Long id = 1L;

        String reportedUserId = "user1"; // 신고받은 사용자 식별자

        String reporterUserId = "user2"; // 신고한 사용자 식별자

        String reason = "욕설 및 비방"; // 신고사유

        //when
        Report report = Report.create(reportedUserId, reporterUserId, reason);

        //then
        assertThat(report).isNotNull();
        assertThat(report.getReportedUserId()).isEqualTo(reportedUserId);
        assertThat(report.getReporterUserId()).isEqualTo(reporterUserId);
        assertThat(report.getReason()).isEqualTo(reason);
        assertThat(report.getReportedAt()).isNotNull();
    }

    @Test
    void create_메소드로_생성시_reportedUserId가_null이거나_비어있으면_예외가_발생한다() {
        // given
        String emptyReportedUserId = "";
        String validReporterUserId = "user2";
        String validReason = "비속어 사용";

        // when & then
        assertThatThrownBy(() -> Report.create(emptyReportedUserId, validReporterUserId, validReason))
                .isInstanceOf(InvalidReportTargetException.class);

        assertThatThrownBy(() -> Report.create(null, validReporterUserId, validReason))
                .isInstanceOf(InvalidReportTargetException.class);
    }

    @Test
    void create_메소드로_생성시_reason이_null이거나_비어있으면_예외가_발생한다() {
        // given
        String validReportedUserId = "user1";
        String validReporterUserId = "user2";
        String emptyReason = "";

        // when & then
        assertThatThrownBy(() -> Report.create(validReportedUserId, validReporterUserId, emptyReason))
                .isInstanceOf(InvalidReportTargetException.class);

        assertThatThrownBy(() -> Report.create(validReportedUserId, validReporterUserId, null))
                .isInstanceOf(InvalidReportTargetException.class);
    }

    @Test
    void of_메서드로_이미_존재하는_데이터를_갖는_Report_객체를_만들_수_있다() {
        // given
        Long id = 100L;
        String reportedUserId = "user10";
        String reporterUserId = "admin";
        String reason = "스팸 신고";
        LocalDateTime reportedAt = LocalDateTime.now().minusDays(1);

        // when
        Report report = Report.of(id, reportedUserId, reporterUserId, reason, reportedAt);

        // then
        assertThat(report.getId()).isEqualTo(id);
        assertThat(report.getReportedUserId()).isEqualTo(reportedUserId);
        assertThat(report.getReporterUserId()).isEqualTo(reporterUserId);
        assertThat(report.getReason()).isEqualTo(reason);
        assertThat(report.getReportedAt()).isEqualTo(reportedAt);
    }




}