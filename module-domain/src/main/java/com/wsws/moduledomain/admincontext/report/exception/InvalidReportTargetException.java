package com.wsws.moduledomain.admincontext.report.exception;

import com.wsws.modulecommon.exception.DomainException;

public class InvalidReportTargetException extends DomainException {

    public static final InvalidReportTargetException EXCEPTION = new InvalidReportTargetException();

    private InvalidReportTargetException() {
        super(AdminErrorCode.EMPTY_REPORTED_USER);}
}
