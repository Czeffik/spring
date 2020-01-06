package com.trzewik.spring.domain.deck

import com.trzewik.spring.domain.deck.Deck.Card
import com.trzewik.spring.domain.deck.Deck.Card.Rank
import com.trzewik.spring.domain.deck.Deck.Card.Suit

trait DeckCreation {

    Deck createDeck(DeckBuilder builder = new DeckBuilder(cards: createCards())) {
        return new DeckImpl(createCards())
    }

    Stack<Card> createCards() {
        Stack<Card> cards = []
        Suit.values().each { suit ->
            Rank.values().each { rank ->
                cards << createCard(new CardBuilder(rank: rank, suit: suit))
            }
        }
        return cards
    }

    Card createCard(Rank rank) {
        return new DeckImpl.CardImpl(
            Suit.SPADE,
            rank
        )
    }

    Card createCard(CardBuilder builder = new CardBuilder()) {
        return new DeckImpl.CardImpl(
            builder.suit,
            builder.rank
        )
    }

    static class CardBuilder {
        Suit suit = Suit.SPADE
        Rank rank = Rank.KING

        CardBuilder() {}

        CardBuilder(Card card) {
            suit = card.suit
            rank = card.rank
        }

        CardBuilder(String suit, String rank){
            this.suit = Suit.valueOf(suit)
            this.rank = Rank.valueOf(rank)
        }
    }

    static class DeckBuilder {
        Stack<Card> cards = []

        DeckBuilder() {}

        DeckBuilder(Deck deck) {
            cards = deck.@cards
        }
    }
}
