package com.trzewik.spring.domain.game

trait ResultCreation {

    Result createResult(ResultCreator creator = new ResultCreator()) {
        return new Result(
            creator.place,
            creator.playerInGame
        )
    }

    static class ResultCreator implements PlayerInGameCreation {
        int place = 1
        PlayerInGame playerInGame = createPlayerInGame()
    }
}
