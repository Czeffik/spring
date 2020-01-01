package com.trzewik.spring.domain.deck;

public interface Deck {
    void shuffle();

    Card take();

    interface Card {
        Suit getSuit();

        Rank getRank();

        boolean isAce();

        interface Rank {
            int getRankValue();

            String getRankName();
        }

        interface Suit {
            String getImage();
        }

    }
}
