package com.trzewik.spring.domain.deck;

import java.util.Arrays;
import java.util.Stack;

public class DeckFactory {

    public static Deck createDeck() {
        return new DeckImpl(createSackWithCards());
    }

    private static Stack<Deck.Card> createSackWithCards() {
        Stack<Deck.Card> stack = new Stack<>();
        Arrays.stream(DeckImpl.CardImpl.RankImpl.values())
            .forEach(rank -> {
                    Arrays.stream(DeckImpl.CardImpl.SuitImpl.values())
                        .forEach(suit -> {
                                stack.push(createCard(suit, rank));
                            }
                        );
                }
            );
        return stack;
    }

    private static Deck.Card createCard(Deck.Card.Suit suit, Deck.Card.Rank rank) {
        return new DeckImpl.CardImpl(suit, rank);
    }
}
