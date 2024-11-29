package com.wsws.moduledomain.user.vo;

import com.wsws.moduledomain.user.exception.InvalidNicknameFormatException;
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
public class Nickname {
    public static final String REGEX = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{2,10}$";
    public static final String ERR_MSG = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Column(name = "nickname", nullable = false, length = 30)
    private String value;

    private Nickname(final String nickname) {
        if (!PATTERN.matcher(nickname).matches()) {
            throw InvalidNicknameFormatException.EXCEPTION;
        }
        this.value = nickname;
    }

    public static Nickname from(final String nickname) {
        return new Nickname(nickname);
    }
}

