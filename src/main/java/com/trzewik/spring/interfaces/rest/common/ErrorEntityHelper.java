package com.trzewik.spring.interfaces.rest.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorEntityHelper {
    public static ResponseEntity<ErrorDto> create(final Exception exception, final HttpStatus status) {
        return new ResponseEntity<>(new ErrorDto(exception, status), status);
    }
}
