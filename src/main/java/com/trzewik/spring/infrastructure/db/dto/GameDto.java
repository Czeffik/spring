package com.trzewik.spring.infrastructure.db.dto;

import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.domain.game.GameFactory;
import com.trzewik.spring.domain.player.Player;
import com.trzewik.spring.infrastructure.db.model.GameEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
public class GameDto {
    private String id;
    private DeckDto deck;
    private String status;
    private List<PlayerGameDto> players;
    private String currentPlayerId;
    private String croupierId;

    public static GameDto from(Game game) {
        return new GameDto(
            game.getId(),
            DeckDto.from(game.getDeck()),
            game.getStatus().name(),
            createPlayers(game),
            game.getCurrentPlayer() == null ? null : game.getCurrentPlayer().getId(),
            game.getCroupier().getId()
        );
    }

    public static GameDto from(GameEntity game) {
        return new GameDto(
            game.getId(),
            game.getDeck(),
            game.getStatus(),
            game.getPlayers().stream().map(PlayerGameDto::from).collect(Collectors.toList()),
            game.getCurrentPlayerId(),
            game.getCroupierId()
        );
    }

    private static List<PlayerGameDto> createPlayers(Game game) {
        return game.getPlayers().stream()
            .map(player -> PlayerGameDto.from(game.getId(), player))
            .collect(Collectors.toList());
    }

    public static Game to(GameDto dto) {
        List<Player> allPlayers = mapTo(dto.getPlayers());
        return GameFactory.createGame(
            dto.id,
            filterOutPlayer(allPlayers, dto.croupierId),
            findPlayer(allPlayers, dto.croupierId),
            DeckDto.to(dto.getDeck()),
            Game.Status.valueOf(dto.getStatus()),
            dto.currentPlayerId == null ? null : findPlayer(allPlayers, dto.currentPlayerId)
        );
    }

    private static List<Player> mapTo(List<PlayerGameDto> players) {
        return players.stream()
            .map(PlayerGameDto::to)
            .collect(Collectors.toList());
    }

    private static Player findPlayer(List<Player> players, String playerId) {
        return players.stream()
            .filter(player -> player.getId().equals(playerId))
            .findFirst()
            .orElseThrow(() -> new PlayerNotFoundException(players, playerId));
    }

    private static List<Player> filterOutPlayer(List<Player> players, String playerIdToExclude) {
        return players.stream()
            .filter(player -> !player.getId().equals(playerIdToExclude))
            .collect(Collectors.toList());
    }

    public static class PlayerNotFoundException extends RuntimeException {
        PlayerNotFoundException(List<Player> players, String playerId) {
            super(String.format("Can not find player with id: [%s] in players: [%s]", playerId, players));
        }
    }

}
