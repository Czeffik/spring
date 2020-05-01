package com.trzewik.spring.domain.game

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class GamePlayerUT extends Specification implements GamePlayerCreation, DeckCreation {
    @Shared
    def playerId = 'player-id'
    @Shared
    def player = createPlayer(new PlayerBuilder(id: playerId))
    @Subject
    GamePlayer gamePlayer = new GamePlayer(player)

    def 'should get player hand'() {
        expect:
        gamePlayer.getHand().size() == 0
    }

    def 'should add card to player hand'() {
        given:
        Card card = createCard()

        when:
        gamePlayer.addCard(card)

        then:
        gamePlayer.@hand.size() == 1
        gamePlayer.@hand.first() == card

        and:
        gamePlayer.getHand().size() == 1
        gamePlayer.getHand().first() == card
    }

    @Unroll
    def 'should calculate hand value correctly. Hand: #STARTING_HAND, Value: #EXPECTED_VALUE'() {
        given:
        STARTING_HAND.each {
            gamePlayer.addCard(it)
        }

        expect:
        gamePlayer.handValue() == EXPECTED_VALUE

        where:
        STARTING_HAND << [
            [],
            [createCard()],
            [createCard(), createCard(new CardBuilder(rank: Rank.ACE))],
            [createCard(new CardBuilder(rank: Rank.ACE))],
            [createCard(), createCard(new CardBuilder(rank: Rank.ACE)), createCard(new CardBuilder(rank: Rank.TWO))]
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
            gamePlayer.addCard(it)
        }

        expect:
        gamePlayer.isLooser() == EXPECTED_VALUE

        where:
        STARTING_HAND << [
            [createCard(), createCard(new CardBuilder(rank: Rank.ACE))],
            [createCard(),
             createCard(new CardBuilder(rank: Rank.TWO)),
             createCard(new CardBuilder(rank: Rank.TEN))]
        ]
        EXPECTED_VALUE << [
            false,
            true
        ]
    }

    def 'should get player id as string'() {
        expect:
        gamePlayer.id != null
        gamePlayer.id instanceof String
    }

    def 'should get player default move = HIT'() {
        expect:
        gamePlayer.getMove() == Move.HIT
    }

    @Unroll
    def 'should set player move = #MOVE and be possible to get it'() {
        when:
        gamePlayer.setMove(MOVE)

        then:
        gamePlayer.@move == MOVE

        and:
        gamePlayer.getMove() == MOVE

        where:
        MOVE << Move.values()
    }

    def 'should create game player with given player'() {
        when:
        def gamePlayer = new GamePlayer(player)

        then:
        gamePlayer.id == player.id
        gamePlayer.name == player.name
        gamePlayer.hand.isEmpty()
        gamePlayer.move == Move.HIT
    }

    def 'should create game player with given player hand and move'() {
        given:
        def hand = [] as Set
        def move = Move.STAND

        when:
        def gamePlayer = new GamePlayer(player, hand, move)

        then:
        gamePlayer.id == player.id
        gamePlayer.name == player.name
        gamePlayer.hand == hand
        gamePlayer.move == move
    }

    def 'should throw exception when trying create game player with null player'() {
        when:
        new GamePlayer(null)

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when trying create game player with null hand'() {
        when:
        new GamePlayer(player, null, Move.HIT)

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when trying create game player with null move'() {
        when:
        new GamePlayer(player, [] as Set, null)

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when trying create game player with null player(with hand and move)'() {
        when:
        new GamePlayer(null, [] as Set, Move.STAND)

        then:
        thrown(NullPointerException)
    }
}
