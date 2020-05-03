package com.trzewik.spring.infrastructure.db.game;

import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.infrastructure.db.player.PlayerDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
public class GameDto {
    private String id;
    private DeckDto deck;
    private String status;
    private Map<PlayerDto, PlayerInGameDto> players;
    private PlayerDto croupier;

    public static GameDto from(Game game) {
        return new GameDto(
            game.getId(),
            DeckDto.from(game.getDeck()),
            game.getStatus().name(),
            createPlayers(game),
            PlayerDto.from(game.getCroupier())
        );
    }

    public static GameDto from(GameEntity gameEntity) {
        return new GameDto(
            gameEntity.getId(),
            gameEntity.getDeck(),
            gameEntity.getStatus(),
            gameEntity.getPlayers().entrySet().stream()
                .collect(Collectors.toMap(
                    e -> PlayerDto.from(e.getKey()),
                    e -> PlayerInGameDto.from(e.getValue())
                )),
            PlayerDto.from(gameEntity.getCroupier())
        );
    }

    private static Map<PlayerDto, PlayerInGameDto> createPlayers(Game game) {
        return game.getPlayers().entrySet().stream()
            .collect(Collectors.toMap(
                e -> PlayerDto.from(e.getKey()),
                e -> PlayerInGameDto.from(game.getId(), e.getKey().getId(), e.getValue())
            ));
    }

    public static Game to(GameDto dto) {
        return new Game(
            dto.id,
            DeckDto.to(dto.getDeck()),
            dto.getPlayers().entrySet().stream().collect(Collectors.toMap(
                e -> PlayerDto.to(e.getKey()),
                e -> PlayerInGameDto.to(e.getValue())
            )),
            PlayerDto.to(dto.getCroupier()),
            Game.Status.valueOf(dto.getStatus())
        );
    }
}
