package com.trzewik.spring.domain.game

trait DeckCreation {

    Deck createDeck(DeckCreator creator = new DeckCreator()) {
        return new Deck(creator.cards)
    }

    static class DeckCreator implements CardCreation {
        Stack<Card> cards = createCards(createDeckCreators())

        DeckCreator() {}

        DeckCreator(Deck deck) {
            cards = deck.getCards()
        }
    }
}
