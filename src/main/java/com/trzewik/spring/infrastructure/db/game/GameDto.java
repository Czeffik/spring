package com.trzewik.spring.infrastructure.db.game;

import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.domain.game.GameFactory;
import com.trzewik.spring.domain.game.GamePlayer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
public class GameDto {
    private String id;
    private DeckDto deck;
    private String status;
    private Set<GamePlayerDto> players;
    private String currentPlayerId;
    private String croupierId;

    public static GameDto from(Game game) {
        return new GameDto(
            game.getId(),
            DeckDto.from(game.getDeck()),
            game.getStatus().name(),
            createPlayers(game),
            game.getCurrentPlayerId(),
            game.getCroupierId()
        );
    }

    public static GameDto from(GameEntity gameEntity) {
        return new GameDto(
            gameEntity.getId(),
            gameEntity.getDeck(),
            gameEntity.getStatus(),
            gameEntity.getPlayers().stream().map(GamePlayerDto::from).collect(Collectors.toSet()),
            gameEntity.getCurrentPlayerId(),
            gameEntity.getCroupierId()
        );
    }

    private static Set<GamePlayerDto> createPlayers(Game game) {
        return game.getPlayers().stream()
            .map(p -> GamePlayerDto.from(game.getId(), p))
            .collect(Collectors.toSet());
    }

    public static Game to(GameDto dto) {
        Set<GamePlayer> allPlayers = mapTo(dto.getPlayers());
        return GameFactory.createGame(
            dto.id,
            allPlayers,
            dto.croupierId,
            DeckDto.to(dto.getDeck()),
            Game.Status.valueOf(dto.getStatus()),
            dto.currentPlayerId
        );
    }

    private static Set<GamePlayer> mapTo(Set<GamePlayerDto> players) {
        return players.stream()
            .map(GamePlayerDto::to)
            .collect(Collectors.toSet());
    }

}
