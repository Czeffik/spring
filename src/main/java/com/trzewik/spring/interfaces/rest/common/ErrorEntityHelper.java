package com.trzewik.spring.interfaces.rest.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorEntityHelper {
    public static ResponseEntity<ErrorDto> create(Exception exception, HttpStatus status) {
        return new ResponseEntity<>(new ErrorDto(exception, status), status);
    }
}
