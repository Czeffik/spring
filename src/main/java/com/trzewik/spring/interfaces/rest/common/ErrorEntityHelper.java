package com.trzewik.spring.interfaces.rest.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorEntityHelper {
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "internal.server.error";

    public static ResponseEntity<ErrorDto> create(final Exception exception, final HttpStatus status) {
        return new ResponseEntity<>(new ErrorDto(exception, status), status);
    }

    public static ResponseEntity<ErrorDto> createInternalServerError() {
        final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(new ErrorDto(INTERNAL_SERVER_ERROR_MESSAGE, status), status);
    }
}
