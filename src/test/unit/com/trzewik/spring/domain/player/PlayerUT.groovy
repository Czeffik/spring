package com.trzewik.spring.domain.player

import spock.lang.Specification
import spock.lang.Subject

class PlayerUT extends Specification {
    static final String playerName = 'Adam'

    @Subject
    Player player = new Player(playerName)

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
        new Player(null, '')

        then:
        thrown(NullPointerException)
    }

    def 'should throw exception when name is null'() {
        when:
        new Player('', null)

        then:
        thrown(NullPointerException)
    }

    def 'should return string representation of player with id and name'() {
        expect:
        player.toString() == "{id=${player.id}, name=${player.name}}"
    }

    def 'should create player with given name'() {
        given:
        def name = 'Adam'

        when:
        def player = new Player(name)

        then:
        player.@name == name
        player.@id != null
    }

    def 'should create croupier with generated id'() {
        when:
        def player = Player.createCroupier()

        then:
        player.@name == 'Croupier'
        player.@id != null
    }

    def 'should create player with given name and id'() {
        given:
        def name = 'Adam'
        def id = '1231'

        when:
        def player = new Player(id, name)

        then:
        player.@name == name
        player.@id == id
    }
}
