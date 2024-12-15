package com.wsws.moduledomain.usercontext.user.encoder;

public interface PasswordEncoder {
    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}
