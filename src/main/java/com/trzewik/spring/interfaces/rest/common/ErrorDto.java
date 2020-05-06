package com.trzewik.spring.interfaces.rest.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorDto {
    private final String message;
    private final int code;
    private final String reason;

    public ErrorDto(Exception exception, HttpStatus status) {
        this.message = exception.getMessage();
        this.code = status.value();
        this.reason = status.getReasonPhrase();
    }
}
