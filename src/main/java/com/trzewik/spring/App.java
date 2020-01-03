package com.trzewik.spring;

import com.trzewik.spring.domain.DomainConfiguration;
import com.trzewik.spring.infrastructure.db.DbConfiguration;
import com.trzewik.spring.interfaces.rest.EmbeddedJetty;
import com.trzewik.spring.interfaces.rest.RestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Import({
    DomainConfiguration.class,
    RestConfiguration.class,
    DbConfiguration.class
})
@PropertySource("classpath:application.properties")
public class App {
    public static void main(String[] args) throws Exception {
        new EmbeddedJetty(9094);
    }
}
