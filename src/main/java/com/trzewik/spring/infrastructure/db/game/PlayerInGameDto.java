package com.trzewik.spring.infrastructure.db.game;

import com.trzewik.spring.domain.game.Card;
import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.domain.game.PlayerInGame;
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
public class PlayerInGameDto {
    private String gameId;
    private String playerId;
    private Set<CardDto> hand;
    private Game.Move move;

    public static PlayerInGameDto from(String gameId, String playerId, PlayerInGame player) {
        return new PlayerInGameDto(
            gameId,
            playerId,
            createHand(player.getHand()),
            player.getMove()
        );
    }

    public static PlayerInGameDto from(PlayerInGameEntity gamePlayerEntity) {
        return new PlayerInGameDto(
            gamePlayerEntity.getId().getGameId(),
            gamePlayerEntity.getId().getPlayerId(),
            gamePlayerEntity.getHand(),
            gamePlayerEntity.getMove()
        );
    }

    public static PlayerInGame toPlayerInGame(PlayerInGameDto dto) {
        return new PlayerInGame(
            toHand(dto.getHand()),
            dto.getMove()
        );
    }

    private static Set<Card> toHand(Set<CardDto> hand) {
        return hand.stream()
            .map(CardDto::toCard)
            .collect(Collectors.toSet());
    }

    private static Set<CardDto> createHand(Set<Card> hand) {
        return hand.stream()
            .map(CardDto::from)
            .collect(Collectors.toSet());
    }
}
