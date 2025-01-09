package com.wsws.moduleapplication.notification.exception;

public class SenderNotFoundException extends RuntimeException {
    public static final SenderNotFoundException EXCEPTION = new SenderNotFoundException();

    private SenderNotFoundException() {
        super(NotificationNotFoundException.EXCEPTION);
    }
}
