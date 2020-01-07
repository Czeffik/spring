package com.trzewik.spring.infrastructure.db.dto;

import com.trzewik.spring.domain.deck.Deck;
import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.domain.player.Player;
import com.trzewik.spring.domain.player.PlayerFactory;
import com.trzewik.spring.infrastructure.db.model.PlayerGameEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerGameDto {
    private String gameId;
    private PlayerDto player;
    private Set<CardDto> hand;
    private String move;

    public static PlayerGameDto from(String gameId, Player player) {
        return new PlayerGameDto(
            gameId,
            PlayerDto.from(player),
            createHand(player.getHand()),
            player.getMove().name()
        );
    }

    public static PlayerGameDto from(PlayerGameEntity entity) {
        return new PlayerGameDto(
            entity.getPlayerGameId().getGameId(),
            PlayerDto.from(entity.getPlayer()),
            entity.getHand(),
            entity.getMove()
        );
    }

    public static Player to(PlayerGameDto dto) {
        return PlayerFactory.createPlayer(
            dto.getPlayer().getId(),
            dto.getPlayer().getName(),
            mapTo(dto.getHand()),
            Game.Move.valueOf(dto.getMove())
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
