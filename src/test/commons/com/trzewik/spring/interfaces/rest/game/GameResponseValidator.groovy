package com.trzewik.spring.interfaces.rest.game

import com.trzewik.spring.domain.game.Card
import com.trzewik.spring.domain.game.Game
import com.trzewik.spring.domain.game.PlayerInGame
import com.trzewik.spring.domain.game.Result
import com.trzewik.spring.domain.player.Player
import com.trzewik.spring.interfaces.rest.ResponseValidator
import io.restassured.response.Response

trait GameResponseValidator extends ResponseValidator {
    boolean validateGameResponse(Response response, Game game) {
        def parsedResponse = parseResponse(response)
        assert parsedResponse.id == game.id
        assert parsedResponse.status == game.status.name()
        assert validatePlayer(parsedResponse.currentPlayer, game.currentPlayer, game.players)
        assert validateCroupier(parsedResponse.croupier, game.croupier, game.players)

        return true
    }

    boolean validatePlayer(parsedPlayer, Player player, Map<Player, PlayerInGame> players) {
        def playerInGame = players.get(player)

        assert parsedPlayer.id == player.id
        assert parsedPlayer.name == player.name
        assert parsedPlayer.move == playerInGame.move?.name()
        assert validatePlayerHand(parsedPlayer.hand, playerInGame)

        return true
    }

    boolean validatePlayerHand(parsedHand, PlayerInGame playerInGame) {
        assert validateHandValue(parsedHand.handValue, playerInGame.handValue())
        assert validateHand(parsedHand.cards, playerInGame.hand)

        return true
    }

    boolean validateHandValue(parsedHandValue, handValue) {
        assert parsedHandValue == handValue

        return true
    }

    boolean validateHand(parsedHand, Set<Card> hand) {
        parsedHand.each { parsedCard ->
            assert hand.any { isCard(parsedCard, it) }
        }

        return true
    }

    boolean validateCroupier(parsedCroupier, Player croupier, Map<Player, PlayerInGame> players) {
        assert parsedCroupier.id == croupier.id
        assert parsedCroupier.name == croupier.name
        assert validateCard(parsedCroupier.card, players.get(croupier).hand.first())

        return true
    }

    boolean validateCard(parsedCard, Card card) {
        assert parsedCard.suit == card.suit.name()
        assert parsedCard.rank == card.rank.name()

        return true
    }

    boolean isCard(parsedCard, Card card) {
        return parsedCard.suit == card.suit.name() &&
            parsedCard.rank == card.rank.name()
    }

    boolean validateResults(Response response, List<Result> results) {
        def parsedResponse = parseResponse(response)
        assert parsedResponse.results.size() == results.size()
        (parsedResponse.results as List).eachWithIndex { parsedResult, index ->
            assert parsedResult.place == results.get(index).place
            assert parsedResult.name == results.get(index).name
            assert validateHandValue(parsedResult.hand.handValue, results.get(index).handValue)
            assert validateHand(parsedResult.hand.cards, results.get(index).hand)
        }

        return true
    }
}
