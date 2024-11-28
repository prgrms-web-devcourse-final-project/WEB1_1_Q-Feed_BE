package com.wsws.moduleapi.global.exception;

import com.wsws.modulecommon.exception.CustomException;
import com.wsws.modulecommon.exception.ErrorInfo;
import com.wsws.modulecommon.exception.GlobalErrorCode;
import com.wsws.moduleapi.global.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    /**
     * Custom 예외
     */
    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<?> customError(CustomException e, HttpServletRequest request) {
        return ResponseEntity.status(e.getStatus())
                .body(
                        ErrorResponse.of(
                                e.getErrorCode().getErrorInfo(),
                                request.getRequestURI(),
                                e.getMessage()));
    }

    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("handleMethodArgumentNotValidException", e);
        return ResponseEntity.status(400)
                .body(
                        ErrorResponse.of(
                                GlobalErrorCode.VALIDATION_ERROR.getErrorInfo(),
                                request.getRequestURI(),
                                e.getBindingResult()));
    }

    /**
     * 그밖에 모든 예외 - 500으로 처리
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> error(Exception e, HttpServletRequest request) {
        log.error("error", e);
        return ResponseEntity.status(500)
                .body(
                        ErrorResponse.of(
                                ErrorInfo.of(500, "INTERNAL_SERVER_ERROR", "알수없는 서버 에러"),
                                request.getRequestURI(),
                                e.getMessage()));
    }
}
