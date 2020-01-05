package com.trzewik.spring.infrastructure.db


import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile

@Profile('test-db')
@Import(DbConfiguration)
@Configuration
class TestDbConfig {
}
