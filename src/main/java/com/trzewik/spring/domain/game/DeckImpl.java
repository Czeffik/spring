package com.trzewik.spring.domain.game;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collections;
import java.util.Stack;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
class DeckImpl implements Deck {
    private final @NonNull Stack<Card> cards;

    @Override
    public void shuffle() {
        Collections.shuffle(cards);
    }

    @Override
    public Card take() {
        return cards.pop();
    }
}
