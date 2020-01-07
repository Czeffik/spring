package com.trzewik.spring;

import com.trzewik.spring.domain.DomainConfiguration;
import com.trzewik.spring.infrastructure.db.DbConfiguration;
import com.trzewik.spring.interfaces.rest.RestConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Import({
    DomainConfiguration.class,
    RestConfiguration.class,
    DbConfiguration.class
})
@PropertySource("classpath:application.properties")
@SpringBootApplication
public class BlackJackApp {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(BlackJackApp.class, args);
    }
}
