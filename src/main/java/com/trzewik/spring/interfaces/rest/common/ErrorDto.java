package com.trzewik.spring.interfaces.rest.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorDto {
    private final String message;
    private final int code;
    private final String reason;

    public ErrorDto(final Exception exception, final HttpStatus status) {
        this.message = exception.getMessage();
        this.code = status.value();
        this.reason = status.getReasonPhrase();
    }

    public ErrorDto(final String message, final HttpStatus status) {
        this.message = message;
        this.code = status.value();
        this.reason = status.getReasonPhrase();
    }
}
