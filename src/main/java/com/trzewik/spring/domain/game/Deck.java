package com.trzewik.spring.domain.game;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.Collections;
import java.util.EmptyStackException;
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

    /**
     * Take single {@link com.trzewik.spring.domain.game.Card} from top of the deck
     *
     * @return {@link com.trzewik.spring.domain.game.Card} from top of the deck
     * @throws Exception when no more cards in deck
     */
    Card take() throws Exception {
        try {
            return cards.pop();
        } catch (EmptyStackException ex) {
            throw new Exception();
        }
    }

    private void shuffle() {
        Collections.shuffle(cards);
    }

    private Stack<Card> createCards() {
        Stack<Card> stack = new Stack<>();
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

    static class Exception extends java.lang.Exception {
        Exception() {
            super("Deck has no more cards - deck is empty!");
        }
    }
}
