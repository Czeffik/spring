package com.trzewik.spring.domain.player


import spock.lang.Specification
import spock.lang.Subject

class PlayerImplUT extends Specification {
    static final String playerName = 'Adam'

    @Subject
    Player player = PlayerFactory.createPlayer(playerName)

    def 'should get player name'() {
        expect:
        player.getName() == playerName
    }

    def 'should get player id as string'() {
        expect:
        player.getId() != null
        player.getId() instanceof String
    }

    def 'should throw exception when id is null'() {
        when:
        new PlayerImpl(null, '')

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when name is null'() {
        when:
        new PlayerImpl('', null)

        then:
        thrown(NullPointerException)
    }

    def 'should return string representation of player with id and name'() {
        expect:
        player.toString() == "{id=${player.id}, name=${player.name}}"
    }
}
