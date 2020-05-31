package com.trzewik.spring.domain.game

trait PlayerInGameCreation {
    PlayerInGame createPlayerInGame(PlayerInGameCreator creator = new PlayerInGameCreator()) {
        return new PlayerInGame(
            creator.playerId,
            creator.name,
            creator.hand,
            creator.move
        )
    }

    static class PlayerInGameCreator {
        String playerId = UUID.randomUUID().toString()
        String name = 'example player name'
        Set<Card> hand = [] as Set
        Game.Move move

        PlayerInGameCreator() {}

        PlayerInGameCreator(PlayerInGame playerInGame, Map map) {
            this.playerId = map.playerId ?: playerInGame.playerId
            this.name = map.name ?: playerInGame.name
            this.hand = map.hand as Set<Card> ?: playerInGame.hand
            this.move = map.move as Game.Move ?: playerInGame.move
        }

        PlayerInGameCreator(PlayerInGame playerInGame) {
            this.playerId = playerInGame.playerId
            this.name = playerInGame.name
            this.hand = playerInGame.hand
            this.move = playerInGame.move
        }
    }
}
