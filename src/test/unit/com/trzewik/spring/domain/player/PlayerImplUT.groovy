package com.trzewik.spring.domain.player

class PlayerImplUT extends ContestantUT {
    static final String playerName = 'Adam'

    def setup(){
        player = PlayerFactory.createPlayer(playerName)
    }

    def 'should get player name'() {
        expect:
        player.getName() == playerName
    }
}
