package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.player.Player;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@EqualsAndHashCode(of = "player")
public class GamePlayer {
    private final @NonNull Player player;
    @Getter
    private final @NonNull Set<Card> hand;
    @Getter
    private @NonNull Move move;

    GamePlayer(@NonNull Player player) {
        this.player = player;
        this.hand = new HashSet<>();
        this.move = Move.HIT; //todo this should not be set?
    }

    public String getName() {
        return player.getName();
    }

    public String getId() {
        return player.getId();
    }

    void addCard(Card card) {
        hand.add(card);
    }

    public int handValue() {
        int sum = calculateHandValueWithoutAce();

        if (sum <= 11 && hasAceInHand()) {
            return sum + 10;
        }

        return sum;
    }

    boolean isLooser() {
        return handValue() > 21;
    }

    void setMove(@NonNull Move move) {
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
            .anyMatch(Card::isAce);
    }
}
