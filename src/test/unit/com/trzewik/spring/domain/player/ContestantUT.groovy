package com.trzewik.spring.domain.player

import com.trzewik.spring.domain.deck.Deck
import com.trzewik.spring.domain.deck.DeckCreation
import com.trzewik.spring.domain.deck.DeckImpl
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

abstract class ContestantUT extends Specification implements DeckCreation {
    @Subject
    Player player

    def 'should get player hand'() {
        expect:
        player.getHand().size() == 0
    }

    def 'should add card to player hand'() {
        given:
        Deck.Card card = createCard()

        when:
        player.addCard(card)

        then:
        player.@hand.size() == 1
        player.@hand.first() == card

        and:
        player.getHand().size() == 1
        player.getHand().first() == card
    }

    @Unroll
    def 'should calculate hand value correctly. Hand: #STARTING_HAND, Value: #EXPECTED_VALUE'() {
        given:
        STARTING_HAND.each {
            player.addCard(it)
        }

        expect:
        player.handValue() == EXPECTED_VALUE

        where:
        STARTING_HAND << [
            [],
            [createCard()],
            [createCard(), createCard(new CardBuilder(rank: DeckImpl.CardImpl.RankImpl.ACE))],
            [createCard(new CardBuilder(rank: DeckImpl.CardImpl.RankImpl.ACE))],
            [createCard(), createCard(new CardBuilder(rank: DeckImpl.CardImpl.RankImpl.ACE)), createCard(new CardBuilder(rank: DeckImpl.CardImpl.RankImpl.TWO))]
        ]
        EXPECTED_VALUE << [
            0,
            10,
            21,
            11,
            13
        ]

    }
}
