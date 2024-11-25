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
    public static final String REGEX = "^(?=.{1,100}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"; //정규식
    public static final String ERR_MSG = "이메일 형식이 올바르지 않습니다.";
    private static final Pattern PATTERN = Pattern.compile(REGEX); // 정규식 활용하여 이메일 형식 설정


    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String value;

    public Email(final String email) { // 이메일은 식별자로서 바뀌면 안되므로 final 처리
        if (!Pattern.matches(REGEX, email)) { //형식이 올바르지 않으면 예외 처리
            throw new IllegalArgumentException(ERR_MSG);
        }
        this.value = email;
    }
}
