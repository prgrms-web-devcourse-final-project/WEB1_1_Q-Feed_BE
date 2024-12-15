package com.wsws.moduledomain.authcontext.auth.repo;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}

