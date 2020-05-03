package com.trzewik.spring.domain.game

import com.trzewik.spring.domain.player.PlayerCreation
import spock.lang.Specification

class ResultsHelperUT extends Specification implements PlayerCreation, PlayerInGameCreation, CardCreation, GameCreation {

    def '''should create results - players which are closest to 21 are top players
            and should be sorted from highest to lowest hand value,
            players which have greater hand value than 21 should be sorted from lowest to highest '''() {
        given:
        def player1 = createPlayer(new PlayerCreator(name: 'player1'))
        def player1InGame = createPlayerInGame(
            new PlayerInGameCreator(
                hand: [
                    createCard(new CardCreator(rank: Card.Rank.ACE)),
                    createCard(new CardCreator(rank: Card.Rank.KING))]
            ))

        and:
        def player2 = createPlayer(new PlayerCreator(name: 'player2'))
        def player2InGame = createPlayerInGame(
            new PlayerInGameCreator(
                hand: [
                    createCard(new CardCreator(rank: Card.Rank.QUEEN)),
                    createCard(new CardCreator(rank: Card.Rank.KING))]
            ))

        and:
        def player3 = createPlayer(new PlayerCreator(name: 'player3'))
        def player3InGame = createPlayerInGame(
            new PlayerInGameCreator(
                hand: [
                    createCard(new CardCreator(rank: Card.Rank.QUEEN)),
                    createCard(new CardCreator(rank: Card.Rank.KING)),
                    createCard(new CardCreator(rank: Card.Rank.TWO))
                ]
            ))

        and:
        def player4 = createPlayer(new PlayerCreator(name: 'player4'))
        def player4InGame = createPlayerInGame(
            new PlayerInGameCreator(
                hand: [
                    createCard(new CardCreator(rank: Card.Rank.QUEEN)),
                    createCard(new CardCreator(rank: Card.Rank.KING)),
                    createCard(new CardCreator(rank: Card.Rank.TEN))
                ]
            ))

        and:
        def game = createGame(new GameCreator(
            players: [
                (player3): player3InGame,
                (player4): player4InGame,
                (player1): player1InGame,
                (player2): player2InGame
            ],
            status: Game.Status.ENDED
        ))

        when:
        def results = ResultsHelper.createResults(game)

        then:
        with(results.get(0)) {
            place == 1
            name == player1.name
            hand == player1InGame.hand
            handValue == player1InGame.handValue()
        }

        and:
        with(results.get(1)) {
            place == 2
            name == player2.name
            hand == player2InGame.hand
            handValue == player2InGame.handValue()
        }

        and:
        with(results.get(2)) {
            place == 3
            name == player3.name
            hand == player3InGame.hand
            handValue == player3InGame.handValue()
        }

        and:
        with(results.get(3)) {
            place == 4
            name == player4.name
            hand == player4InGame.hand
            handValue == player4InGame.handValue()
        }
    }

    def 'should trow exception when trying get results from game which was not ended'() {
        given:
        def game = createGame(new GameCreator(
            status: Game.Status.STARTED
        ))
        when:
        ResultsHelper.createResults(game)
        then:
        Result.Exception ex = thrown()
        ex.message == 'Results are available only when game finished. Please continue auction.'
    }
}
