package com.trzewik.spring.infrastructure.db.dto;

import com.trzewik.spring.domain.deck.Deck;
import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.domain.player.Player;
import com.trzewik.spring.domain.player.PlayerFactory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerGameDto {
    private final String gameId;
    private final PlayerDto player;
    private final Set<CardDto> hand;
    private final Game.Move move;

    public static PlayerGameDto from(String gameId, Player player) {
        return new PlayerGameDto(
            gameId,
            PlayerDto.from(player),
            createHand(player.getHand()),
            player.getMove()
        );
    }

    public static Player to(PlayerGameDto dto) {
        return PlayerFactory.createPlayer(
            dto.getPlayer().getId(),
            dto.getPlayer().getName(),
            mapTo(dto.getHand()),
            dto.getMove()
        );
    }

    private static Set<Deck.Card> mapTo(Set<CardDto> hand) {
        return hand.stream()
            .map(CardDto::to)
            .collect(Collectors.toSet());
    }

    private static Set<CardDto> createHand(Set<Deck.Card> hand) {
        return hand.stream()
            .map(CardDto::from)
            .collect(Collectors.toSet());
    }
}
