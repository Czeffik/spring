package com.trzewik.spring.domain.common

import spock.lang.Specification

class CardFactoryUT extends Specification {

    def 'should create card'() {
        given:
        Deck.Card.Suit suit = Deck.Card.Suit.CLUB
        Deck.Card.Rank rank = Deck.Card.Rank.FIVE

        when:
        Deck.Card card = CardFactory.create(suit, rank)

        then:
        card.getRank() == rank
        card.getSuit() == suit
    }

    def 'should throw exception when rank is null'(){
        when:
        CardFactory.create(Deck.Card.Suit.CLUB, null)

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when suit is null'(){
        when:
        CardFactory.create(null, Deck.Card.Rank.FIVE)

        then:
        thrown(NullPointerException)
    }
}
