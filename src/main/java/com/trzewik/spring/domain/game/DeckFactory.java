package com.trzewik.spring.domain.game;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Stack;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeckFactory {

    public static Deck createDeck() {
        return createDeck(createSackWithCards());
    }

    public static Deck createDeck(Stack<Card> cards) {
        return new DeckImpl(cards);
    }

    private static Stack<Card> createSackWithCards() {
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
