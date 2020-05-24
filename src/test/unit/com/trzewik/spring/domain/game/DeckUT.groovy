package com.trzewik.spring.domain.game

import spock.lang.Specification
import spock.lang.Subject

class DeckUT extends Specification implements DeckCreation, CardCreation {

    @Subject
    def deck = new Deck()

    def 'should create deck with 52 cards'() {
        expect:
            deck.cards.size() == 52
    }

    def 'should be possible to take one card from deck'() {
        when:
            def takenCard = deck.take()
        then:
            def cards = deck.cards
            cards.size() == 51
        and:
            !cards.contains(takenCard)
    }

    def 'should throw exception when cards are null'() {
        when:
            new Deck(null)
        then:
            NullPointerException ex = thrown()
            ex.message == 'cards is marked non-null but is null'
    }

    def 'should create deck with given stack of cards'() {
        given:
            def cards = [] as Stack
        when:
            def deck = new Deck(cards)
        then:
            deck.cards.is(cards)
    }

    def 'should throw exception when taking card but stack with cards is empty'() {
        given:
            def cards = [] as Stack
        and:
            def deck = new Deck(cards)
        when:
            deck.take()
        then:
            thrown(EmptyStackException)
    }

    def 'should create shuffled deck with 52 cards with all cards combinations, and without duplicates'() {
        given:
            def expectedCards = createCards(createDeckCreators())
        expect:
            def cards = deck.cards
            cards.size() == 52
            cards.containsAll(expectedCards)
            expectedCards.containsAll(cards)
        and: 'cards with random order'
            cards != expectedCards
    }

    def 'deck to string method should print return string representation of all cards in deck'() {
        given:
            def deckWithOrderedCards = createDeck()
        expect:
            deckWithOrderedCards.toString().contains('Deck(cards=[')
    }
}
