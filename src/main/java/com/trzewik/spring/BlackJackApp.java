package com.trzewik.spring;

import com.trzewik.spring.infrastructure.db.DomainConfiguration;
import com.trzewik.spring.infrastructure.db.DbConfiguration;
import com.trzewik.spring.interfaces.rest.RestConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({
    DomainConfiguration.class,
    RestConfiguration.class,
    DbConfiguration.class,
    PropertyPlaceholderAutoConfiguration.class
})
@SpringBootConfiguration
public class BlackJackApp {
    public static void main(String[] args) {
        SpringApplication.run(BlackJackApp.class, args);
    }
}
