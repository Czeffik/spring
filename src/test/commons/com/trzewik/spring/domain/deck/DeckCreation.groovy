package com.trzewik.spring.domain.deck

import com.trzewik.spring.domain.deck.Deck.Card.Rank

trait DeckCreation {

    Deck createDeck(){
        return DeckFactory.createDeck()
    }

    Deck.Card createCard(Rank rank){
        return new DeckImpl.CardImpl(
            Deck.Card.Suit.SPADE,
            rank
        )
    }

    Deck.Card createCard(CardBuilder builder = new CardBuilder()){
        return new DeckImpl.CardImpl(
            builder.suit,
            builder.rank
        )
    }

    static class CardBuilder{
        Deck.Card.Suit suit = Deck.Card.Suit.SPADE
        Deck.Card.Rank rank = Deck.Card.Rank.KING
    }
}
