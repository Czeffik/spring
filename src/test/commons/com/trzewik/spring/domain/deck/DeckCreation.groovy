package com.trzewik.spring.domain.deck

import com.trzewik.spring.domain.deck.Deck.Card.Rank
import com.trzewik.spring.domain.deck.Deck.Card.Suit

trait DeckCreation {

    Deck createDeck() {
        return DeckFactory.createDeck()
    }

    Deck.Card createCard(Rank rank) {
        return new DeckImpl.CardImpl(
            Suit.SPADE,
            rank
        )
    }

    Deck.Card createCard(CardBuilder builder = new CardBuilder()) {
        return new DeckImpl.CardImpl(
            builder.suit,
            builder.rank
        )
    }

    static class CardBuilder {
        Suit suit = Suit.SPADE
        Rank rank = Rank.KING
    }
}
