package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.common.Deck;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.Set;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "playerId")
public class GamePlayerImpl implements GamePlayer {
    private final @NonNull String playerId;
    private final @NonNull Set<Deck.Card> hand;
    private @NonNull Game.Move move;

    @Override
    public void addCard(Deck.Card card) {
        hand.add(card);
    }

    @Override
    public int handValue() {
        int sum = calculateHandValueWithoutAce();

        if (sum <= 11 && hasAceInHand()) {
            return sum + 10;
        }

        return sum;
    }

    @Override
    public boolean isLooser() {
        return handValue() > 21;
    }

    @Override
    public void setMove(@NonNull Game.Move move) {
        this.move = move;
    }

    @Override
    public String toString() {
        return String.format("{hand=%s, move=%s}", hand, move);
    }

    private int calculateHandValueWithoutAce() {
        return hand.stream()
            .map(card -> card.getRank().getRankValue())
            .reduce(0, Integer::sum);
    }

    private boolean hasAceInHand() {
        return hand.stream()
            .anyMatch(Deck.Card::isAce);
    }
}
