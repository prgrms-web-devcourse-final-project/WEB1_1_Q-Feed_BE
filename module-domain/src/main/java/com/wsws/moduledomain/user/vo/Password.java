package com.wsws.moduledomain.user.vo;

import com.wsws.moduledomain.user.PasswordEncoder;
import com.wsws.moduledomain.user.exception.InvalidPasswordFormatException;
import com.wsws.moduledomain.auth.exception.PasswordMismatchException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {
    public static final String REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,20}";
    private static final Pattern PATTERN = Pattern.compile(REGEX); // 정적 필드로 사용 -> 대량 검증시 성능 저하 덜함

    @Column(nullable = false, name="password")
    private String value;

    private Password(final String value) {
        this.value = value;
    }

    public static Password encode(final String rawPassword, final PasswordEncoder passwordEncoder) {
        validate(rawPassword);
        return new Password(passwordEncoder.encode(rawPassword));
    }

    public void matches(final String rawPassword, final PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(rawPassword, this.value)) {
            throw PasswordMismatchException.EXCEPTION; // 예외 처리
        }
    }

    public static void validate(final String password) {
        if (!PATTERN.matcher(password).matches()) {
            throw InvalidPasswordFormatException.EXCEPTION;
        }
    }


}
