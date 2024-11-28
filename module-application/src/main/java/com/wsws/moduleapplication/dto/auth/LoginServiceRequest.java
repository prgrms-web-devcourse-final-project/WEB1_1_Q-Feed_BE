package com.wsws.moduleapplication.dto.auth;

public record LoginServiceRequest(
        String email,
        String password
) {
}
