package com.trzewik.spring.domain.game

import com.trzewik.spring.domain.common.Deck

trait ResultCreation implements GameCreation {

    List<Result> createResults(int number) {
        List<Result> results = []
        number.times {
            results << createResult(new ResultBuilder(
                place: (it + 1),
                player: createGamePlayer(
                    new GamePlayerBuilder(
                        hand: [createCard(Deck.Card.Rank.QUEEN), createCard(Deck.Card.Rank.FOUR)]
                    )
                )
            ))
        }
        return results
    }

    Result createResult(ResultBuilder builder = new ResultBuilder()) {
        return new Result(
            builder.place,
            builder.player
        )
    }

    static class ResultBuilder implements GamePlayerCreation {
        int place = 1
        GamePlayer player = createGamePlayer()
    }
}
