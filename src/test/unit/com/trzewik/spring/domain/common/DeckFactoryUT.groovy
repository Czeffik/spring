package com.trzewik.spring.domain.common

import com.trzewik.spring.domain.deck.DeckCreation
import spock.lang.Specification

class DeckFactoryUT extends Specification implements DeckCreation {

    def 'should create deck with 52 cards with all cards combinations, and without duplicates'() {
        given:
        Stack<Deck.Card> expectedCards = []
        Deck.Card.Suit.values().each { suit ->
            Deck.Card.Rank.values().each { rank ->
                expectedCards << createCard(new CardBuilder(rank: rank, suit: suit))
            }
        }
        when:
        Deck deck = DeckFactory.createDeck()

        then:
        Stack<Deck.Card> cards = deck.@cards
        cards.size() == 52
        cards.containsAll(expectedCards)
        expectedCards.containsAll(cards)
    }

    def 'should create deck with given stack of cards'() {
        given:
        Stack<Deck.Card> cards = []
        Deck.Card.Suit.values().each { suit ->
            cards << createCard(new CardBuilder(suit: suit))
        }

        when:
        Deck deck = DeckFactory.createDeck(cards)

        then:
        def cardsInDeck = deck.@cards
        cardsInDeck.is(cards)
        cardsInDeck.size() == 4
    }

    def 'should throw exception when cards are null'() {
        when:
        DeckFactory.createDeck(null)

        then:
        thrown(NullPointerException)
    }
}
