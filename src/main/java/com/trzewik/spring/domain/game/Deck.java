package com.trzewik.spring.domain.game;

import java.util.Stack;

public interface Deck {
    Stack<Card> getCards();

    void shuffle();

    Card take();
}
