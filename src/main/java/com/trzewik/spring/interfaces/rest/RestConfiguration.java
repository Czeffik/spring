package com.trzewik.spring.interfaces.rest;

import com.trzewik.spring.domain.game.GameService;
import com.trzewik.spring.domain.player.PlayerService;
import com.trzewik.spring.interfaces.rest.game.GameController;
import com.trzewik.spring.interfaces.rest.player.PlayerController;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletPath;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class RestConfiguration {
    @Bean
    GameController gameController(GameService gameService) {
        return new GameController(gameService);
    }

    @Bean
    PlayerController playerController(PlayerService playerService) {
        return new PlayerController(playerService);
    }

    @Bean
    ServletWebServerFactory servletWebServerFactory() {
        return new JettyServletWebServerFactory();
    }

    @Bean
    DispatcherServletPath dispatcherServletPath(DispatcherServlet dispatcherServlet) {
        return new DispatcherServletRegistrationBean(dispatcherServlet, "/");
    }

    @Bean
    DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }
}
