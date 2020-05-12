package com.trzewik.spring.domain.game

import spock.lang.Specification
import spock.lang.Unroll

class PlayerInGameUT extends Specification implements CardCreation {

    def 'should create game with move as null and empty hand'() {
        given:
            def playerInGame = new PlayerInGame()
        expect:
            playerInGame.hand.isEmpty()
            playerInGame.move == null
    }

    def 'should throw exception when creating with null hand'() {
        when:
            new PlayerInGame(null, null)
        then:
            NullPointerException ex = thrown()
            ex.message == 'hand is marked non-null but is null'
    }

    def 'should create with given hand and move'() {
        given:
            def hand = [] as Set
            def move = Game.Move.STAND
        when:
            def playerInGame = new PlayerInGame(hand, move)
        then:
            playerInGame.hand == hand
            playerInGame.move == move
    }

    def 'should return string representation of object'() {
        expect:
            new PlayerInGame().toString() == '{hand=[], move=null}'
    }

    def 'players in game with same hand and move should be equls'() {
        given:
            def hand = [createCard()] as Set
            def move = Game.Move.HIT
        and:
            def player1 = new PlayerInGame(hand, move)
        and:
            def player2 = new PlayerInGame(hand, move)
        expect:
            player1 == player2
    }

    @Unroll
    def 'player with hand: #HAND and move: #MOVE should be not equals to player with hand: #HAND2 and move: #MOVE2'() {
        given:
            def player1 = new PlayerInGame(HAND, MOVE)
        and:
            def player2 = new PlayerInGame(HAND2, MOVE2)
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
            def player = new PlayerInGame()
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
            def player = new PlayerInGame()
        when:
            player.addCard(null)
        then:
            NullPointerException ex = thrown()
            ex.message == 'card is marked non-null but is null'
    }

    @Unroll
    def 'should return is looser: #RESULT when hand is: #HAND'() {
        given:
            def player = new PlayerInGame(HAND, Game.Move.STAND)
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
            def player = new PlayerInGame(HAND, Game.Move.STAND)
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
