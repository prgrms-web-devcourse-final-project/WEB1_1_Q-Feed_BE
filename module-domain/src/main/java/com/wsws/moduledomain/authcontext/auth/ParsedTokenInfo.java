package com.wsws.moduledomain.authcontext.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ParsedTokenInfo {
    private final String userId;
    private final String role;
}
