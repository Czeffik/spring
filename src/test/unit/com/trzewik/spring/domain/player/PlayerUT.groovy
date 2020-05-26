package com.trzewik.spring.domain.player

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class PlayerUT extends Specification implements PlayerCommandCreation {
    static final String playerName = 'Adam'

    @Subject
    Player player = new Player(createPlayerCommand(new PlayerCommandCreator(name: playerName)))

    def 'should get player name'() {
        expect:
            player.name == playerName
    }

    def 'should get player id as string'() {
        expect:
            player.id != null
            player.id instanceof String
    }

    def 'should throw exception when id is null'() {
        when:
            new Player(null, '')

        then:
            NullPointerException ex = thrown()
            ex.message == 'id is marked non-null but is null'
    }

    def 'should throw exception when name is null'() {
        when:
            new Player('', null)

        then:
            NullPointerException ex = thrown()
            ex.message == 'name is marked non-null but is null'
    }

    def 'should throw exception when form is command one arg constructor'() {
        when:
            new Player(null)

        then:
            NullPointerException ex = thrown()
            ex.message == 'command is marked non-null but is null'
    }

    def 'should return string representation of player with id and name'() {
        expect:
            player.toString() == "{id=${player.id}, name=${player.name}}"
    }

    def 'should create player from given form with generated id'() {
        given:
            def form = createPlayerCommand()

        when:
            def player = new Player(form)

        then:
            player.name == form.getName()
            player.id
    }

    def 'should create croupier with generated id'() {
        when:
            def player = Player.createCroupier()

        then:
            player.name == 'CROUPIER'
            player.id == 'CROUPIER-ID'
    }

    def 'should create player with given name and id'() {
        given:
            def name = 'Adam'
            def id = '1231'

        when:
            def player = new Player(id, name)

        then:
            player.name == name
            player.id == id
    }

    @Unroll
    def 'player with id: #ID and name: #NAME should not be equals to player with id: #ID2 and name: #NAME2'() {
        given:
            def player1 = new Player(ID, NAME)
        and:
            def player2 = new Player(ID2, NAME2)
        expect:
            player1 != player2
        where:
            ID    | NAME       | ID2   | NAME2
            '123' | playerName | '124' | playerName
            '123' | playerName | '123' | 'other name'
            '124' | playerName | '123' | 'other name'
    }

    def 'players with same name and id should be equals'() {
        given:
            def id = '123'
        and:
            def player1 = new Player(id, playerName)
        and:
            def player2 = new Player(id, playerName)
        expect:
            player1 == player2
    }
}
