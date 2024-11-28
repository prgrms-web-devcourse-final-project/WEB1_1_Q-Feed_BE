package com.wsws.moduleapplication.auth.dto;

public record LoginServiceRequest(
        String email,
        String password
) {
}
