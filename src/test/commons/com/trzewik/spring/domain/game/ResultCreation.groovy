package com.trzewik.spring.domain.game

import com.trzewik.spring.domain.deck.Deck
import com.trzewik.spring.domain.player.PlayerCreation

trait ResultCreation implements GameCreation {

    List<Result> createResults(int number) {
        List<Result> results = []
        number.times {
            results << createResult(new ResultBuilder(
                place: (it + 1),
                player: new PlayerBuilder(
                    hand: [createCard(Deck.Card.Rank.QUEEN), createCard(Deck.Card.Rank.FOUR)]
                )
            ))
        }
        return results
    }

    Result createResult(ResultBuilder builder = new ResultBuilder()) {
        return new Result(
            builder.place,
            createPlayer(builder.player)
        )
    }

    static class ResultBuilder implements PlayerCreation {
        int place = 1
        PlayerBuilder player = new PlayerBuilder()

    }
}
