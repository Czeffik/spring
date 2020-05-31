package com.trzewik.spring.infrastructure;

import com.trzewik.spring.infrastructure.application.ApplicationConfiguration;
import com.trzewik.spring.infrastructure.db.DbConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
    ApplicationConfiguration.class,
    DbConfiguration.class
})
@Configuration
public class InfrastructureConfiguration {
}
