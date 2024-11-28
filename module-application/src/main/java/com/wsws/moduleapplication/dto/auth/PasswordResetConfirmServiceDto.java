package com.wsws.moduleapplication.dto.auth;

public record PasswordResetConfirmServiceDto(String email, String code, String newPassword) {}
