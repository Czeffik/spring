package com.trzewik.spring.domain.game;

import com.google.common.collect.ImmutableSet;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.Set;

@Getter
@EqualsAndHashCode
public class PlayerInGame {
    private final @NonNull String playerId;
    private final @NonNull String name;
    private final @NonNull Set<Card> hand;
    private final Game.Move move;

    public PlayerInGame(
        @NonNull final String playerId,
        @NonNull final String name,
        @NonNull final Set<Card> hand,
        final Game.Move move
    ) {
        this.playerId = playerId;
        this.name = name;
        this.hand = ImmutableSet.copyOf(hand);
        this.move = move;
    }

    public PlayerInGame(
        @NonNull final String playerId,
        @NonNull final String name
    ) {
        this(
            playerId,
            name,
            ImmutableSet.<Card>builder().build(),
            null
        );
    }

    private PlayerInGame(
        @NonNull final PlayerInGame playerInGame,
        @NonNull final Set<Card> hand,
        final Game.Move move
    ) {
        this(
            playerInGame.getPlayerId(),
            playerInGame.getName(),
            hand,
            move
        );
    }

    @Override
    public String toString() {
        return String.format("{playerId=%s, name=%s, hand=%s, move=%s}",
            playerId, name, hand, move);
    }

    PlayerInGame addCard(@NonNull final Card card) {
        final Set<Card> handWithNewCard = ImmutableSet.<Card>builder()
            .addAll(this.hand)
            .add(card)
            .build();

        return new PlayerInGame(this, handWithNewCard, this.move);
    }

    PlayerInGame changeMove(@NonNull final Game.Move move) {
        return new PlayerInGame(this, this.hand, move);
    }

    public int handValue() {
        final int sum = calculateHandValueWithoutAce();

        if (sum <= 11 && hasAceInHand()) {
            return sum + 10;
        }

        return sum;
    }

    boolean isLooser() {
        return handValue() > 21;
    }

    private int calculateHandValueWithoutAce() {
        return hand.stream()
            .map(card -> card.getRank().getRankValue())
            .reduce(0, Integer::sum);
    }

    private boolean hasAceInHand() {
        return hand.stream()
            .anyMatch(Card::isAce);
    }
}
