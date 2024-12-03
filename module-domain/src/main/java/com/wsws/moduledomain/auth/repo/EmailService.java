package com.wsws.moduledomain.auth.repo;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}

