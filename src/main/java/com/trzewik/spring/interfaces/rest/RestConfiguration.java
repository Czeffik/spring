package com.trzewik.spring.interfaces.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class RestConfiguration {
    @Bean
    Controller controller() {
        return new Controller();
    }
}
