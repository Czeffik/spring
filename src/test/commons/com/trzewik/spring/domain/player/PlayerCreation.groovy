package com.trzewik.spring.domain.player

import com.trzewik.spring.domain.deck.Deck
import com.trzewik.spring.domain.game.Game


trait PlayerCreation {

    Player createPlayer(PlayerBuilder builder = new PlayerBuilder()) {
        return new PlayerImpl(
            builder.id,
            builder.name,
            builder.hand,
            builder.move
        )
    }

    List<Player> createPlayers(int playerNumber = 2) {
        def players = []
        playerNumber.times { players << createPlayer() }
        return players
    }

    static class PlayerBuilder {
        String id = UUID.randomUUID().toString()
        String name = 'example name'
        Set<Deck.Card> hand = [] as Set
        Game.Move move = Game.Move.HIT

        PlayerBuilder() {}

        PlayerBuilder(Player player) {
            id = player.id
            name = player.name
            hand = player.hand
            move = player.move
        }
    }

}
