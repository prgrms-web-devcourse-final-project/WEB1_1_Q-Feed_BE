package com.wsws.moduledomain.chat.exception;

import com.wsws.modulecommon.exception.ApplicationException;
import com.wsws.modulecommon.exception.DomainException;

public class SelfSelectionNotAllowedException extends DomainException {
    public static final SelfSelectionNotAllowedException EXCEPTION = new SelfSelectionNotAllowedException();

    private SelfSelectionNotAllowedException() {
        super(ChatErrorCode.SELF_SELECTION_NOT_ALLOWED);
    }
}