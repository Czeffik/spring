package com.trzewik.spring;

import com.trzewik.spring.infrastructure.DomainConfiguration;
import com.trzewik.spring.infrastructure.InfrastructureConfiguration;
import com.trzewik.spring.interfaces.rest.RestConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Import;

@Import({
    DomainConfiguration.class,
    RestConfiguration.class,
    InfrastructureConfiguration.class
})
@SpringBootConfiguration
public class BlackJackApp {
    public static void main(String[] args) {
        SpringApplication.run(BlackJackApp.class, args);
    }
}
