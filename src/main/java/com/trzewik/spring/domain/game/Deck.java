package com.trzewik.spring.domain.game;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Deck {
    private final @NonNull Stack<Card> cards;

    Deck() {
        this.cards = createCards();
        shuffle();
    }

    Card take() {
        return cards.pop();
    }

    private void shuffle() {
        Collections.shuffle(cards);
    }

    private Stack<Card> createCards() {
        final Stack<Card> stack = new Stack<>();
        Arrays.stream(Card.Suit.values())
            .forEach(suit -> {
                    Arrays.stream(Card.Rank.values())
                        .forEach(rank -> {
                                stack.push(new Card(suit, rank));
                            }
                        );
                }
            );
        return stack;
    }
}
