package com.wsws.moduleapi.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.wsws.moduleapi.exception.ErrorInfo;
import com.wsws.moduleapi.exception.GlobalErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ErrorResponse {

    private String path;

    private String errorCode;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<FieldError> errors;


    private LocalDateTime timeStamp;

    private ErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorResponse(String path, String errorCode, String message) {
        this.path = path;
        this.errorCode = errorCode;
        this.message = message;
        this.timeStamp = LocalDateTime.now();
    }

    private ErrorResponse(String path, String errorCode, String message, List<FieldError> errors) {
        this.path = path;
        this.errorCode = errorCode;
        this.message = message;
        this.errors = errors;
        this.timeStamp = LocalDateTime.now();
    }

    public static ErrorResponse of(ErrorInfo info, String path, String message) {
        return new ErrorResponse(path, info.errorCode(), message);
    }

    public static ErrorResponse of(String errorCode, String message) {
        return new ErrorResponse(errorCode, message);
    }


    public static ErrorResponse of(ErrorInfo info, String path, BindingResult bindingResult) {
        return new ErrorResponse(path, info.errorCode(), info.message(), FieldError.of(bindingResult));
    }

    /**
     * Validation 에러의 정보를 담고 있는 클래스
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        private FieldError(final String field, final String value, final String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream().map(error -> new FieldError(
                    error.getField(),
                    error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                    error.getDefaultMessage())).collect(Collectors.toList());
        }
    }
}
