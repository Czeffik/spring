package com.trzewik.spring.domain.player

trait PlayerCreation {

    Player createPlayer(PlayerCreator creator = new PlayerCreator()) {
        return new Player(
            creator.id,
            creator.name
        )
    }

    List<Player> createPlayers(List<PlayerCreator> creators = [PlayerCreator.croupier(), new PlayerCreator()]) {
        return creators.collect { createPlayer(it) }
    }

    static class PlayerCreator {
        String id = UUID.randomUUID().toString()
        String name = 'example player name'

        PlayerCreator() {}

        PlayerCreator(Player player) {
            this.id = player.id
            this.name = player.name
        }

        PlayerCreator(Player player, Map map) {
            this.id = map.id ?: player.id
            this.name = map.name ?: player.name
        }

        static PlayerCreator croupier() {
            return new PlayerCreator(Player.createCroupier())
        }
    }

}
