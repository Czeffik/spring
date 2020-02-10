package com.trzewik.spring.infrastructure.db.game;

import com.trzewik.spring.domain.game.Deck;
import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.domain.game.GamePlayer;
import com.trzewik.spring.domain.game.GamePlayerFactory;
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
public class GamePlayerDto {
    private String gameId;
    private String playerId;
    private Set<CardDto> hand;
    private String move;

    public static GamePlayerDto from(String gameId, GamePlayer player) {
        return new GamePlayerDto(
            gameId,
            player.getPlayerId(),
            createHand(player.getHand()),
            player.getMove().name()
        );
    }

    public static GamePlayerDto from(GamePlayerEntity gamePlayerEntity) {
        return new GamePlayerDto(
            gamePlayerEntity.getId().getGameId(),
            gamePlayerEntity.getId().getPlayerId(),
            gamePlayerEntity.getHand(),
            gamePlayerEntity.getMove()
        );
    }

    public static GamePlayer to(GamePlayerDto dto) {
        return GamePlayerFactory.create(
            dto.getPlayerId(),
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
