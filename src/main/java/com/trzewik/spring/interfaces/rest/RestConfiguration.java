package com.trzewik.spring.interfaces.rest;

import com.trzewik.spring.domain.board.Board;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class RestConfiguration {
    @Bean
    BoardController controller(Board board) {
        return new BoardController(board);
    }
}
