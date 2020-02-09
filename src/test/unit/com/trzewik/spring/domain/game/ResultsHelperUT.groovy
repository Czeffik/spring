package com.trzewik.spring.domain.game

import com.trzewik.spring.domain.common.Deck
import com.trzewik.spring.domain.deck.DeckCreation
import spock.lang.Specification

class ResultsHelperUT extends Specification implements DeckCreation, GamePlayerCreation {

    def '''should create results - players which are closest to 21 are top players
            and should be sorted from highest to lowest hand value,
            players which have greater hand value than 21 should be sorted from lowest to highest '''() {
        given:
        def player1 = createGamePlayer()
        player1.addCard(createCard(Deck.Card.Rank.ACE))
        player1.addCard(createCard(Deck.Card.Rank.KING))

        and:
        def player2 = createGamePlayer()
        player2.addCard(createCard(Deck.Card.Rank.QUEEN))
        player2.addCard(createCard(Deck.Card.Rank.KING))

        and:
        def player3 = createGamePlayer()
        player3.addCard(createCard(Deck.Card.Rank.QUEEN))
        player3.addCard(createCard(Deck.Card.Rank.KING))
        player3.addCard(createCard(Deck.Card.Rank.TWO))

        and:
        def player4 = createGamePlayer()
        player4.addCard(createCard(Deck.Card.Rank.QUEEN))
        player4.addCard(createCard(Deck.Card.Rank.KING))
        player4.addCard(createCard(Deck.Card.Rank.TEN))

        and:
        def players = [player3, player2, player4, player1] as Set

        when:
        def results = ResultsHelper.createResults(players)

        then:
        def first = results.get(0)
        first.place == 1
        first.player == player1

        and:
        def second = results.get(1)
        second.place == 2
        second.player == player2

        and:
        def third = results.get(2)
        third.place == 3
        third.player == player3

        and:
        def fourth = results.get(3)
        fourth.place == 4
        fourth.player == player4
    }
}
