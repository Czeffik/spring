package com.trzewik.spring.domain.game

import spock.lang.Specification
import spock.lang.Unroll

class CardUT extends Specification {

    def 'should create card'() {
        given:
            def suit = Card.Suit.CLUB
            def rank = Card.Rank.FIVE

        when:
            def card = new Card(suit, rank)

        then:
            card.rank == rank
            card.suit == suit
    }

    def 'should throw exception when rank is null'() {
        when:
            new Card(Card.Suit.CLUB, null)

        then:
            NullPointerException ex = thrown()
            ex.message == 'rank is marked non-null but is null'
    }

    def 'should throw exception when suit is null'() {
        when:
            new Card(null, Card.Rank.FIVE)

        then:
            NullPointerException ex = thrown()
            ex.message == 'suit is marked non-null but is null'
    }

    def 'card to string method should get suit image and rank name'() {
        given:
            def suit = Card.Suit.CLUB
            def rank = Card.Rank.FIVE
            def card = new Card(suit, rank)
        expect:
            card.toString() == "${suit.image}${rank.rankName}"
    }

    def 'cards with same suit and rank should be equals'() {
        given:
            def card1 = new Card(Card.Suit.CLUB, Card.Rank.KING)
        and:
            def card2 = new Card(Card.Suit.CLUB, Card.Rank.KING)
        expect:
            card1 == card2
    }

    @Unroll
    def 'cards with suit: #SUIT and rank: #RANK should be not equals to card with suit: #SUIT2 and rank: #RANK2'() {
        given:
            def card1 = new Card(SUIT, RANK)
        and:
            def card2 = new Card(SUIT2, RANK2)
        expect:
            card1 != card2
        where:
            SUIT           | RANK           | SUIT2           | RANK2
            Card.Suit.CLUB | Card.Rank.KING | Card.Suit.SPADE | Card.Rank.KING
            Card.Suit.CLUB | Card.Rank.KING | Card.Suit.CLUB  | Card.Rank.ACE

    }

    @Unroll
    def 'should check that card: #CARD is ace - #RESULT'() {
        expect:
            CARD.isAce() == RESULT
        where:
            CARD                                     || RESULT
            new Card(Card.Suit.CLUB, Card.Rank.ACE)  || true
            new Card(Card.Suit.CLUB, Card.Rank.KING) || false
    }
}
