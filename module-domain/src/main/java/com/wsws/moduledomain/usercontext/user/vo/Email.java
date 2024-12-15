package com.wsws.moduledomain.usercontext.user.vo;

import com.wsws.moduledomain.usercontext.user.exception.InvalidEmailFormatException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.regex.Pattern;


@Getter
@EqualsAndHashCode
public class Email {
    public static final String REGEX = "^(?=.{1,100}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    private String value;

    // Private 생성자
    private Email(final String email) {
        if (!PATTERN.matcher(email).matches()) {
            throw InvalidEmailFormatException.EXCEPTION;
        }
        this.value = email;
    }

    // 정적 팩토리 메서드
    public static Email from(final String email) {
        return new Email(email);
    }
}
