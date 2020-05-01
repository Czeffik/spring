package com.trzewik.spring.domain.game;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class Deck {
    private final @NonNull Stack<Card> cards;

    public Deck() {
        cards = createCards();
        shuffle();
    }

    public Card take() {
        return cards.pop();
    }

    private void shuffle() {
        Collections.shuffle(cards);
    }

    private Stack<Card> createCards() {
        Stack<Card> stack = new Stack<>();
        Arrays.stream(Suit.values())
            .forEach(suit -> {
                    Arrays.stream(Rank.values())
                        .forEach(rank -> {
                                stack.push(new Card(suit, rank));
                            }
                        );
                }
            );
        return stack;
    }
}
