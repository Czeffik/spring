package com.trzewik.spring.domain.player

import com.trzewik.spring.domain.game.GamePlayer

trait PlayerCreation {

    Player createPlayer(PlayerBuilder builder = new PlayerBuilder()) {
        return new Player(
            builder.id,
            builder.name
        )
    }

    List<Player> createPlayers(int playerNumber = 2) {
        def players = []
        playerNumber.times { players << createPlayer() }
        return players
    }

    List<Player> createPlayers(List<String> playerIds) {
        def players = []
        playerIds.each { players << createPlayer(new PlayerBuilder(id: it, name: it)) }
        return players
    }

    static class PlayerBuilder {
        String id = UUID.randomUUID().toString()
        String name = 'example name'

        PlayerBuilder() {}

        PlayerBuilder(Player player) {
            id = player.id
            name = player.name
        }

        PlayerBuilder(GamePlayer player) {
            id = player.id
            name = player.name
        }
    }

}
