package com.wsws.moduledomain.admincontext.report.exception;

import com.wsws.modulecommon.exception.DomainException;

public class InvalidReportReasonException extends DomainException {

    public static final InvalidReportReasonException EXCEPTION = new InvalidReportReasonException();

    private InvalidReportReasonException() {
        super(AdminErrorCode.EMPTY_REPORT);}

}
