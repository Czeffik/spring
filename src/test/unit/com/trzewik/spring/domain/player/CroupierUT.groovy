package com.trzewik.spring.domain.player

class CroupierUT extends ContestantUT{
    def setup(){
        player = PlayerFactory.createCroupier()
    }

    def 'should get player name'() {
        expect:
        player.getName() == 'Croupier'
    }
}
