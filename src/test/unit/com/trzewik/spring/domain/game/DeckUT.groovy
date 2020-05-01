package com.trzewik.spring.domain.game

import spock.lang.Specification
import spock.lang.Subject

class DeckUT extends Specification implements DeckCreation {

    @Subject
    Deck deck = new Deck()

    def 'should create deck with 52 cards'() {
        expect:
        (deck as Deck).cards.size() == 52
    }

    def 'should take one card from deck'() {
        when:
        Card takenCard = deck.take()

        then:
        Stack<Card> cards = (deck as Deck).cards
        cards.size() == 51

        and:
        !cards.contains(takenCard)
    }

    def 'should throw exception when cards are null'() {
        when:
        new Deck(null)

        then:
        thrown(NullPointerException)
    }

    def 'should create deck with given stack of cards'() {
        given:
        Stack<Card> cards = []
        Suit.values().each { suit ->
            cards << createCard(new CardBuilder(suit: suit))
        }

        when:
        Deck deck = new Deck(cards)

        then:
        def cardsInDeck = deck.@cards
        cardsInDeck.is(cards)
        cardsInDeck.size() == 4
    }

    def 'should create shuffled deck with 52 cards with all cards combinations, and without duplicates'() {
        given:
        Stack<Card> expectedCards = []
        Suit.values().each { suit ->
            Rank.values().each { rank ->
                expectedCards << createCard(new CardBuilder(rank: rank, suit: suit))
            }
        }

        expect:
        Stack<Card> cards = deck.@cards
        cards.size() == 52
        cards.containsAll(expectedCards)
        expectedCards.containsAll(cards)

        and: 'cards with random order'
        cards != expectedCards
    }
}
