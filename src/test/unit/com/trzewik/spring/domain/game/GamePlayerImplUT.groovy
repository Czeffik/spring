package com.trzewik.spring.domain.game

import com.trzewik.spring.domain.common.Deck
import com.trzewik.spring.domain.deck.DeckCreation
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class GamePlayerImplUT extends Specification implements GamePlayerCreation, DeckCreation{
    static final String playerId = 'player-id'
    @Subject
    GamePlayer player = GamePlayerFactory.create(playerId)

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
            [createCard(), createCard(new CardBuilder(rank: Deck.Card.Rank.ACE))],
            [createCard(new CardBuilder(rank: Deck.Card.Rank.ACE))],
            [createCard(), createCard(new CardBuilder(rank: Deck.Card.Rank.ACE)), createCard(new CardBuilder(rank: Deck.Card.Rank.TWO))]
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
            [createCard(), createCard(new CardBuilder(rank: Deck.Card.Rank.ACE))],
            [createCard(),
             createCard(new CardBuilder(rank: Deck.Card.Rank.TWO)),
             createCard(new CardBuilder(rank: Deck.Card.Rank.TEN))]
        ]
        EXPECTED_VALUE << [
            false,
            true
        ]
    }

    def 'should get player id as string'() {
        expect:
        player.playerId != null
        player.playerId instanceof String
    }

    def 'should get player default move = HIT'() {
        expect:
        player.getMove() == Game.Move.HIT
    }

    @Unroll
    def 'should set player move = #MOVE and be possible to get it'() {
        when:
        player.setMove(MOVE)

        then:
        player.@move == MOVE

        and:
        player.getMove() == MOVE

        where:
        MOVE << Game.Move.values()
    }
}
