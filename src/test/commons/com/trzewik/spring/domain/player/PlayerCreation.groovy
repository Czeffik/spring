package com.trzewik.spring.domain.player

import com.trzewik.spring.domain.game.Game


trait PlayerCreation {

    Player createPlayer(PlayerBuilder builder = new PlayerBuilder()) {
        return new PlayerImpl(
            builder.id,
            builder.name,
            builder.move
        )
    }

    List<Player> createPlayers(int playerNumber = 2) {
        def players = []
        playerNumber.times { players << createPlayer() }
        return players
    }

    static class PlayerBuilder {
        UUID id = UUID.randomUUID()
        String name = 'example name'
        Game.Move move = null
    }

}
