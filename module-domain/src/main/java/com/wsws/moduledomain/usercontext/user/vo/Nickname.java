package com.wsws.moduledomain.usercontext.user.vo;

import com.wsws.moduledomain.usercontext.user.exception.InvalidNicknameFormatException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.regex.Pattern;


@EqualsAndHashCode
@Getter
public class Nickname {
    public static final String REGEX = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{2,20}$";
    public static final String ERR_MSG = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

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

