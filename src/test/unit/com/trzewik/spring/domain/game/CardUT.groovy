package com.trzewik.spring.domain.game

import spock.lang.Specification
import spock.lang.Unroll

class CardUT extends Specification {

    def 'should create card'() {
        given:
        Suit suit = Suit.CLUB
        Rank rank = Rank.FIVE

        when:
        Card card = new Card(suit, rank)

        then:
        card.getRank() == rank
        card.getSuit() == suit
    }

    def 'should throw exception when rank is null'() {
        when:
        new Card(Suit.CLUB, null)

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when suit is null'() {
        when:
        new Card(null, Rank.FIVE)

        then:
        thrown(NullPointerException)
    }

    @Unroll
    def 'should check that card is ace - #RESULT'() {
        expect:
        CARD.isAce() == RESULT
        where:
        CARD                           || RESULT
        new Card(Suit.CLUB, Rank.ACE)  || true
        new Card(Suit.CLUB, Rank.KING) || false
    }
}
