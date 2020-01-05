package com.trzewik.spring.domain.deck;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Stack;

import static com.trzewik.spring.domain.deck.CardFactory.create;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeckFactory {

    public static Deck createDeck() {
        return createDeck(createSackWithCards());
    }

    public static Deck createDeck(Stack<Deck.Card> cards) {
        return new DeckImpl(cards);
    }

    private static Stack<Deck.Card> createSackWithCards() {
        Stack<Deck.Card> stack = new Stack<>();
        Arrays.stream(Deck.Card.Suit.values())
            .forEach(suit -> {
                    Arrays.stream(Deck.Card.Rank.values())
                        .forEach(rank -> {
                                stack.push(create(suit, rank));
                            }
                        );
                }
            );
        return stack;
    }
}
