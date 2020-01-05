package com.trzewik.spring.domain.player;

import com.trzewik.spring.domain.deck.Deck;
import com.trzewik.spring.domain.game.Game;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
class PlayerImpl implements Player {
    private final @NonNull String id;
    private final @NonNull String name;
    private final @NonNull Set<Deck.Card> hand;
    private Game.Move move;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<Deck.Card> getHand() {
        return hand;
    }

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
    public Game.Move getMove() {
        return move;
    }

    @Override
    public void setMove(@NonNull Game.Move move) {
        this.move = move;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PlayerImpl player = (PlayerImpl) obj;
        return id.equals(player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("{id=%s, name=%s, hand=%s}", id, name, hand.toString());
    }
}
