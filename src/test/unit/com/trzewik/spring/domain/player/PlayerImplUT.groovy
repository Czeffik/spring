package com.trzewik.spring.domain.player

import com.trzewik.spring.domain.deck.Deck
import com.trzewik.spring.domain.deck.DeckCreation
import com.trzewik.spring.domain.deck.DeckImpl
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class PlayerImplUT extends Specification implements DeckCreation{
    static final String playerName = 'Adam'
    @Subject
    Player player = PlayerFactory.createPlayer(playerName)


    def 'should get player name'() {
        expect:
        player.getName() == playerName
    }

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

    def 'should check that player has more than 21 points and is loser. Hand: #STARTING_HAND, Value: #EXPECTED_VALUE'() {
        given:
        STARTING_HAND.each {
            player.addCard(it)
        }

        expect:
        player.isLooser() == EXPECTED_VALUE

        where:
        STARTING_HAND << [
            [createCard(), createCard(new CardBuilder(rank: DeckImpl.CardImpl.RankImpl.ACE))],
            [createCard(),
             createCard(new CardBuilder(rank: DeckImpl.CardImpl.RankImpl.TWO)),
             createCard(new CardBuilder(rank: DeckImpl.CardImpl.RankImpl.TEN))]
        ]
        EXPECTED_VALUE << [
            false,
            true
        ]

    }
}
