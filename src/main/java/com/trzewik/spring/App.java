package com.trzewik.spring;

import com.trzewik.spring.domain.DomainConfiguration;
import com.trzewik.spring.infrastructure.db.DbConfiguration;
import com.trzewik.spring.interfaces.rest.RestConfiguration;
import com.trzewik.spring.interfaces.rest.WebAppInitializer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.Arrays;

@Import({
    DomainConfiguration.class,
    RestConfiguration.class,
    DbConfiguration.class
})
@PropertySource("classpath:application.properties")
public class App {
    public static void main(String[] args) {
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(App.class);
//        Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
    }
}
