package com.trzewik.spring.interfaces.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    public String index() {
        return "{\"dupa\": \"greeting\"}";
    }
}
