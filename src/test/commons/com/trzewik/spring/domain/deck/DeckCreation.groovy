package com.trzewik.spring.domain.deck

trait DeckCreation {

    Deck createDeck(){
        return DeckFactory.createDeck()
    }

    Deck.Card createCard(CardBuilder builder = new CardBuilder()){
        return new DeckImpl.CardImpl(
            builder.suit,
            builder.rank
        )
    }

    static class CardBuilder{
        Deck.Card.Suit suit = DeckImpl.CardImpl.SuitImpl.SPADE
        Deck.Card.Rank rank = DeckImpl.CardImpl.RankImpl.KING
    }
}
