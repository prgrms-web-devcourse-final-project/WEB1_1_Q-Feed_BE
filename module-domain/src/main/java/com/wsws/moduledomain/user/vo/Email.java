package com.wsws.moduledomain.user.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {
    public static final String REGEX = "^(?=.{1,100}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    public static final String ERR_MSG = "이메일 형식이 올바르지 않습니다.";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String value;

    // Private 생성자
    private Email(final String email) {
        if (!PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException(ERR_MSG);
        }
        this.value = email;
    }

    // 정적 팩토리 메서드
    public static Email from(final String email) {
        return new Email(email);
    }
}
