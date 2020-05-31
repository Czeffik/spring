package com.trzewik.spring.domain.game

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class PlayerInGameUT extends Specification implements PlayerInGameCreation, CardCreation {

    @Shared
    def PLAYER_NAME = 'NAME'
    @Shared
    def PLAYER_ID = 'ID'

    def 'should create game with move as null and empty hand, and name and id'() {
        given:
            def playerInGame = new PlayerInGame(PLAYER_ID, PLAYER_NAME)
        expect:
            with(playerInGame) {
                hand.isEmpty()
                move == null
                name == PLAYER_NAME
                playerId == PLAYER_ID
            }
    }

    def 'should throw exception when creating with null id'() {
        when:
            new PlayerInGame(null, '')
        then:
            NullPointerException ex = thrown()
            ex.message == 'playerId is marked non-null but is null'
    }

    def 'should throw exception when creating with null name'() {
        when:
            new PlayerInGame('', null)
        then:
            NullPointerException ex = thrown()
            ex.message == 'name is marked non-null but is null'
    }

    def 'should throw exception when creating with null id all args'() {
        when:
            new PlayerInGame(null, '', [] as Set, null)
        then:
            NullPointerException ex = thrown()
            ex.message == 'playerId is marked non-null but is null'
    }

    def 'should throw exception when creating with null name all args'() {
        when:
            new PlayerInGame('', null, [] as Set, null)
        then:
            NullPointerException ex = thrown()
            ex.message == 'name is marked non-null but is null'
    }

    def 'should throw exception when creating with null hand all args'() {
        when:
            new PlayerInGame('', '', null, null)
        then:
            NullPointerException ex = thrown()
            ex.message == 'hand is marked non-null but is null'
    }

    def 'should create with given hand and move'() {
        given:
            def givenHand = [] as Set
            def givenMove = Game.Move.STAND
        when:
            def playerInGame = new PlayerInGame(PLAYER_ID, PLAYER_NAME, givenHand, givenMove)
        then:
            with(playerInGame) {
                hand == givenHand
                move == givenMove
                name == PLAYER_NAME
                playerId == PLAYER_ID
            }
    }

    def 'should return string representation of object'() {
        expect:
            new PlayerInGame(PLAYER_ID, PLAYER_NAME).toString() == "{playerId=$PLAYER_ID, name=$PLAYER_NAME, hand=[], move=null}".toString()
    }

    def 'same players in game should be equals'() {
        given:
            def hand = [createCard()] as Set
            def move = Game.Move.HIT
        and:
            def player1 = new PlayerInGame(PLAYER_ID, PLAYER_NAME, hand, move)
        and:
            def player2 = new PlayerInGame(PLAYER_ID, PLAYER_NAME, hand, move)
        expect:
            player1 == player2
    }

    @Unroll
    def 'player with hand: #HAND and move: #MOVE should be not equals to player with hand: #HAND2 and move: #MOVE2'() {
        given:
            def player1 = new PlayerInGame(PLAYER_ID, PLAYER_NAME, HAND, MOVE)
        and:
            def player2 = new PlayerInGame(PLAYER_ID, PLAYER_NAME, HAND2, MOVE2)
        expect:
            player1 != player2
        where:
            HAND                  | MOVE          | HAND2                 | MOVE2
            [createCard()] as Set | Game.Move.HIT | [createCard()] as Set | Game.Move.STAND
            [createCard()] as Set | Game.Move.HIT | [] as Set             | Game.Move.HIT
            [createCard()] as Set | Game.Move.HIT | [] as Set             | Game.Move.STAND
    }

    def 'should add card to hand'() {
        given:
            def card = createCard()
        and:
            def player = new PlayerInGame(PLAYER_ID, PLAYER_NAME)
        when:
            def playerWithCard = player.addCard(card)
        then:
            playerWithCard != player
        and:
            playerWithCard.hand.size() == 1
        and:
            playerWithCard.hand.first().is(card)
    }

    def 'should throw exception when adding card which is null'() {
        given:
            def player = new PlayerInGame(PLAYER_ID, PLAYER_NAME)
        when:
            player.addCard(null)
        then:
            NullPointerException ex = thrown()
            ex.message == 'card is marked non-null but is null'
    }

    @Unroll
    def 'should return is looser: #RESULT when hand is: #HAND'() {
        given:
            def player = new PlayerInGame(PLAYER_ID, PLAYER_NAME, HAND, Game.Move.STAND)
        expect:
            player.isLooser() == RESULT
        where:
            HAND                                                                                                                                                             || RESULT
            [createCard()] as Set                                                                                                                                            || false
            [createCard(new CardCreator(rank: Card.Rank.KING)), createCard(new CardCreator(rank: Card.Rank.QUEEN)), createCard(new CardCreator(rank: Card.Rank.TWO))] as Set || true
            [createCard(new CardCreator(rank: Card.Rank.KING)), createCard(new CardCreator(rank: Card.Rank.ACE))] as Set                                                     || false
    }

    @Unroll
    def 'for hand: #HAND should return hand value: #EXPECTED_VALUE'() {
        given:
            def player = new PlayerInGame(PLAYER_ID, PLAYER_NAME, HAND, Game.Move.STAND)
        expect:
            player.handValue() == EXPECTED_VALUE
        where:
            HAND                                                                                                                                                             || EXPECTED_VALUE
            [createCard(new CardCreator(rank: Card.Rank.ACE))] as Set                                                                                                        || 11
            [createCard(new CardCreator(rank: Card.Rank.KING)), createCard(new CardCreator(rank: Card.Rank.QUEEN)), createCard(new CardCreator(rank: Card.Rank.TWO))] as Set || 22
            [createCard(new CardCreator(rank: Card.Rank.KING)), createCard(new CardCreator(rank: Card.Rank.ACE))] as Set                                                     || 21
            [createCard(new CardCreator(rank: Card.Rank.KING)), createCard(new CardCreator(rank: Card.Rank.QUEEN)), createCard(new CardCreator(rank: Card.Rank.ACE))] as Set || 21
            [createCard(new CardCreator(rank: Card.Rank.ACE)), createCard(new CardCreator(rank: Card.Rank.SEVEN))] as Set                                                    || 18
    }
}
