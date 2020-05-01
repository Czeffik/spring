package com.trzewik.spring.interfaces.rest;

import com.trzewik.spring.domain.game.GameService;
import com.trzewik.spring.domain.player.PlayerService;
import com.trzewik.spring.interfaces.rest.game.GameController;
import com.trzewik.spring.interfaces.rest.player.PlayerController;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Import({
    SwaggerConfiguration.class,
    DispatcherServletAutoConfiguration.class,
    ServletWebServerFactoryAutoConfiguration.class
})
@Configuration
@EnableWebMvc
public class RestConfiguration implements WebMvcConfigurer {
    @Bean
    GameController gameController(GameService gameService, PlayerService playerService) {
        return new GameController(gameService, playerService);
    }

    @Bean
    PlayerController playerController(PlayerService playerService) {
        return new PlayerController(playerService);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
