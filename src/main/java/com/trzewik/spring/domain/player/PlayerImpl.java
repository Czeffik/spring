package com.trzewik.spring.domain.player;

import com.trzewik.spring.domain.deck.Deck;
import com.trzewik.spring.domain.game.Game;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
class PlayerImpl implements Player {
    private final Set<Deck.Card> hand = new HashSet<>();
    private final @NonNull UUID id;
    private final @NonNull String name;
    private Game.Move move;

    @Override
    public String getId() {
        return id.toString();
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerImpl player = (PlayerImpl) o;
        return id.equals(player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("{id=%s, name=%s, hand=%s}", id.toString(), name, hand.toString());
    }
}
