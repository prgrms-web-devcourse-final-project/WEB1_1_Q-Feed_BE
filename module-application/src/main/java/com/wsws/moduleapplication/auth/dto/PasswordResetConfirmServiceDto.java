package com.wsws.moduleapplication.auth.dto;

public record PasswordResetConfirmServiceDto(String email, String code, String newPassword) {}
