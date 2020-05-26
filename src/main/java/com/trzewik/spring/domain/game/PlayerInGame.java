package com.trzewik.spring.domain.game;

import com.google.common.collect.ImmutableSet;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.Set;

@Getter
@EqualsAndHashCode
public class PlayerInGame {
    private final @NonNull Set<Card> hand;
    private final Game.Move move;

    public PlayerInGame(@NonNull final Set<Card> hand, final Game.Move move) {
        this.hand = ImmutableSet.copyOf(hand);
        this.move = move;
    }

    PlayerInGame() {
        this.hand = ImmutableSet.<Card>builder().build();
        this.move = null;
    }

    @Override
    public String toString() {
        return String.format("{hand=%s, move=%s}", hand, move);
    }

    PlayerInGame addCard(@NonNull final Card card) {
        final Set<Card> handWithNewCard = ImmutableSet.<Card>builder()
            .addAll(this.hand)
            .add(card)
            .build();

        return new PlayerInGame(handWithNewCard, this.move);
    }

    PlayerInGame changeMove(@NonNull final Game.Move move) {
        return new PlayerInGame(this.hand, move);
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
