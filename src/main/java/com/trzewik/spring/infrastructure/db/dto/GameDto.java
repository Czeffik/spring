package com.trzewik.spring.infrastructure.db.dto;

import com.trzewik.spring.common.PlayerUtil;
import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.domain.game.GameFactory;
import com.trzewik.spring.domain.player.Player;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GameDto {
    private final String id;
    private final DeckDto deck;
    private final Game.Status status;
    private final List<PlayerGameDto> players;
    private final String currentPlayerId;
    private final String croupierId;

    public static GameDto from(Game game) {
        return new GameDto(
            game.getId(),
            DeckDto.from(game.getDeck()),
            game.getStatus(),
            createPlayers(game),
            game.getCurrentPlayer().getId(),
            game.getCroupier().getId()
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
            PlayerUtil.filterOutPlayer(allPlayers, dto.croupierId),
            PlayerUtil.findPlayer(allPlayers, dto.croupierId),
            DeckDto.to(dto.getDeck()),
            dto.getStatus(),
            PlayerUtil.findPlayer(allPlayers, dto.currentPlayerId)
        );
    }

    private static List<Player> mapTo(List<PlayerGameDto> players) {
        return players.stream()
            .map(PlayerGameDto::to)
            .collect(Collectors.toList());
    }
}
