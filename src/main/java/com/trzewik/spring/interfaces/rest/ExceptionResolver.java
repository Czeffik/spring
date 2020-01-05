package com.trzewik.spring.interfaces.rest;

import com.trzewik.spring.domain.game.GameException;
import com.trzewik.spring.domain.game.GameRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionResolver extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {GameException.class})
    protected ResponseEntity<Object> handleBadRequest(GameException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {
        GameRepository.GameNotFoundException.class
    })
    protected ResponseEntity<Object> handleNotFound(Exception ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
