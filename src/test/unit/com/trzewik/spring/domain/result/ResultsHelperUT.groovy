package com.trzewik.spring.domain.result

import com.trzewik.spring.domain.common.Deck
import com.trzewik.spring.domain.deck.DeckCreation
import com.trzewik.spring.domain.player.Player
import com.trzewik.spring.domain.player.PlayerCreation
import spock.lang.Specification

class ResultsHelperUT extends Specification implements DeckCreation, PlayerCreation {

    def '''should create results - players which are closest to 21 are top players
            and should be sorted from higest to lowest hand value,
            players which have greater hand value than 21 should be sorted from lowest to highest '''() {
        given:
        Player player1 = createPlayer()
        player1.addCard(createCard(Deck.Card.Rank.ACE))
        player1.addCard(createCard(Deck.Card.Rank.KING))

        and:
        Player player2 = createPlayer()
        player2.addCard(createCard(Deck.Card.Rank.QUEEN))
        player2.addCard(createCard(Deck.Card.Rank.KING))

        and:
        Player player3 = createPlayer()
        player3.addCard(createCard(Deck.Card.Rank.QUEEN))
        player3.addCard(createCard(Deck.Card.Rank.KING))
        player3.addCard(createCard(Deck.Card.Rank.TWO))

        and:
        Player player4 = createPlayer()
        player4.addCard(createCard(Deck.Card.Rank.QUEEN))
        player4.addCard(createCard(Deck.Card.Rank.KING))
        player4.addCard(createCard(Deck.Card.Rank.TEN))

        and:
        def players = [player3, player2, player4, player1]

        when:
        def results = ResultsHelper.createResults(players)

        then:
        def first = results.get(0)
        first.@place == 1
        first.@player == player1

        and:
        def second = results.get(1)
        second.@place == 2
        second.@player == player2

        and:
        def third = results.get(2)
        third.@place == 3
        third.@player == player3

        and:
        def fourth = results.get(3)
        fourth.@place == 4
        fourth.@player == player4
    }
}
