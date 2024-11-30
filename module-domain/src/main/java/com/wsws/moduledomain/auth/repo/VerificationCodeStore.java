package com.wsws.moduledomain.auth.repo;

import java.util.Optional;

public interface VerificationCodeStore {
    void saveCode(String key, String code, long ttlInSeconds);
    Optional<String> getCode(String key);
    void deleteCode(String key);
}
