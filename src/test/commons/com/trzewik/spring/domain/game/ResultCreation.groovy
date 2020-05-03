package com.trzewik.spring.domain.game

import com.trzewik.spring.domain.player.Player
import com.trzewik.spring.domain.player.PlayerCreation

trait ResultCreation {

    Result createResult(ResultCreator creator = new ResultCreator()) {
        return new Result(
            creator.place,
            creator.player,
            creator.playerInGame
        )
    }

    static class ResultCreator implements PlayerCreation, PlayerInGameCreation {
        int place = 1
        Player player = createPlayer()
        PlayerInGame playerInGame = createPlayerInGame()
    }
}
