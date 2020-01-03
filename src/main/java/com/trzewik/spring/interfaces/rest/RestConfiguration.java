package com.trzewik.spring.interfaces.rest;

import com.trzewik.spring.domain.service.GameService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Configuration
@EnableWebMvc
public class RestConfiguration {
    @Bean
    GameController gameController(GameService service) {
        return new GameController(service);
    }

    @Bean
    ResponseEntityExceptionHandler responseEntityExceptionHandler() {
        return new ExceptionResolver();
    }
}
