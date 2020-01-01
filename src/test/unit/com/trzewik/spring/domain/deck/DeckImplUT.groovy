package com.trzewik.spring.domain.deck

import spock.lang.Specification
import spock.lang.Subject


class DeckImplUT extends Specification {

    @Subject
    Deck deck = DeckFactory.createDeck()

    def 'should create deck with 52 cards'() {
        expect:
        (deck as DeckImpl).cards.size() == 52
    }

    def 'should shuffle deck'(){
        given:
        Deck otherDeck = DeckFactory.createDeck()

        expect:
        (deck as DeckImpl).cards == (otherDeck as DeckImpl).cards

        when:
        deck.shuffle()

        then:
        (deck as DeckImpl).cards != (otherDeck as DeckImpl).cards
    }

    def 'should take one card from deck'(){
        when:
        Deck.Card takenCard = deck.take()

        then:
        Stack<Deck.Card> cards = (deck as DeckImpl).cards
        cards.size() == 51

        and:
        !cards.contains(takenCard)
    }

}
