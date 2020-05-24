package com.trzewik.spring.domain.game

trait GamePropertiesCreation {

    GameProperties createGameProperties(GamePropertiesCreator creator = new GamePropertiesCreator()) {
        return new GameProperties(
            creator.maximumPlayers
        )
    }

    static class GamePropertiesCreator {
        int maximumPlayers = 5
    }
}
