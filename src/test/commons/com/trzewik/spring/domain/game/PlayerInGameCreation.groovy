package com.trzewik.spring.domain.game

trait PlayerInGameCreation {
    PlayerInGame createPlayerInGame(PlayerInGameCreator creator = new PlayerInGameCreator()) {
        return new PlayerInGame(
            creator.hand,
            creator.move
        )
    }

    static class PlayerInGameCreator {
        Set<Card> hand = [] as Set
        Game.Move move

        PlayerInGameCreator() {}

        PlayerInGameCreator(PlayerInGame playerInGame, Map map) {
            this.hand = map.hand as Set<Card> ?: playerInGame.hand
            this.move = map.move as Game.Move ?: playerInGame.move
        }

        PlayerInGameCreator(PlayerInGame playerInGame) {
            this.hand = playerInGame.hand
            this.move = playerInGame.move
        }
    }
}
