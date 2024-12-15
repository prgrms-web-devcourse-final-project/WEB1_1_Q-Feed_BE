package com.wsws.moduleapplication.authcontext.dto;

public record LoginServiceRequest(
        String email,
        String password
) {
}
