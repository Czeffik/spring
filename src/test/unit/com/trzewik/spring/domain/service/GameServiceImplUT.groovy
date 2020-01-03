package com.trzewik.spring.domain.service

import com.trzewik.spring.domain.game.Game
import com.trzewik.spring.domain.game.GameException
import com.trzewik.spring.domain.game.GameRepository
import com.trzewik.spring.domain.game.Result
import com.trzewik.spring.domain.player.Player
import com.trzewik.spring.domain.player.PlayerRepository
import spock.lang.Specification
import spock.lang.Subject

class GameServiceImplUT extends Specification {

    GameRepository gameRepo = new GameRepositoryMock()
    PlayerRepository playerRepo = new PlayerRepositoryMock()

    @Subject
    GameService service = GameServiceFactory.createService(gameRepo, playerRepo)

    def 'should create game and save in game repository'() {
        when:
        Game savedGame = service.createGame()

        then:
        gameRepo.repository.size() == 1
        gameRepo.findById(savedGame.getId()).get().is(savedGame)
    }

    def 'should add new player to game with gameId and save player in player repository'() {
        given:
        Game game = service.createGame()

        when:
        Player createdPlayer = service.addPlayer(game.id, 'Adam')

        then:
        playerRepo.getRepository().size() == 1
        game.@players.size() == 1
        playerRepo.findById(createdPlayer.id).get().is(createdPlayer)
    }

    def 'should NOT add player to repository and throw exception when can not find game with gameId'() {
        when:
        service.addPlayer('wrong-game-id', 'Adam')

        then:
        GameRepository.GameNotFoundException ex = thrown()
        ex.message == 'Game with id: [wrong-game-id] not found.'

        and:
        playerRepo.repository.isEmpty()
    }

    def 'should NOT add player to repository and throw exception when trying add player to game which is started'() {
        given:
        Game game = service.createGame()
        service.addPlayer(game.id, 'Adam')
        service.startGame(game.id)

        when:
        service.addPlayer(game.id, 'Niet')

        then:
        thrown(GameException)

        and:
        playerRepo.repository.size() == 1
    }

    def 'should find game by id in repository and start it'() {
        given:
        Game game = service.createGame()
        Player player = service.addPlayer(game.id, 'Adam')

        when:
        service.startGame(game.id)

        then:
        game.status == Game.Status.STARTED
    }

    def 'should NOT start game and throw exception when can not find game with gameId'() {
        when:
        service.startGame('wrong-game-id')

        then:
        GameRepository.GameNotFoundException ex = thrown()
        ex.message == 'Game with id: [wrong-game-id] not found.'
    }

    def 'should NOT start game when no players added to game and throw exception'(){
        given:
        Game game = service.createGame()

        when:
        service.startGame(game.id)

        then:
        thrown(GameException)

        and:
        game.status == Game.Status.NOT_STARTED
    }

    def 'should throw exception when trying start game which is already started'(){
        given:
        Game game = service.createGame()
        service.addPlayer(game.id, 'Adam')
        service.startGame(game.id)

        when:
        service.startGame(game.id)

        then:
        thrown(GameException)
    }

    def 'should find game and player in repository and player should make move when game is started'() {
        given:
        Game game = service.createGame()
        Player player = service.addPlayer(game.id, 'Adam')
        service.startGame(game.id)

        when:
        service.makeMove(game.id, player.id, Game.Move.HIT)

        then:
        player.hand.size() == 3
    }

    def 'should throw exception when can not find player with playerId in player repository when trying make move'(){
        given:
        Game game = service.createGame()
        Player player = service.addPlayer(game.id, 'Adam')
        service.startGame(game.id)

        when:
        service.makeMove(game.id, 'wrong-player-id', Game.Move.HIT)

        then:
        PlayerRepository.PlayerNotFoundException ex = thrown()
        ex.message == 'Player with id: [wrong-player-id] not found.'
    }

    def 'should throw exception when can not find game with gameId in game repository when trying make move'(){
        given:
        Game game = service.createGame()
        Player player = service.addPlayer(game.id, 'Adam')
        service.startGame(game.id)

        when:
        service.makeMove('wrong-game-id', player.id, Game.Move.HIT)

        then:
        GameRepository.GameNotFoundException ex = thrown()
        ex.message == 'Game with id: [wrong-game-id] not found.'
    }

    def 'should not be possible to make move when game was not started'(){
        given:
        Game game = service.createGame()
        Player player = service.addPlayer(game.id, 'Adam')

        when:
        service.makeMove(game.id, player.id, Game.Move.HIT)

        then:
        thrown(GameException)
    }

    def 'should not be possible to make move when game was ended'(){
        given:
        Game game = service.createGame()
        Player player = service.addPlayer(game.id, 'Adam')
        service.startGame(game.id)
        service.makeMove(game.id, player.id, Game.Move.STAND)

        when:
        service.makeMove(game.id, player.id, Game.Move.HIT)

        then:
        thrown(GameException)

        and:
        player.hand.size() == 2
    }

    def 'should find game in repository and return game results when game is ended'() {
        given:
        Game game = service.createGame()
        Player player = service.addPlayer(game.id, 'Adam')
        service.startGame(game.id)
        service.makeMove(game.id, player.id, Game.Move.STAND)

        when:
        List<Result> results = service.getGameResults(game.id)

        then:
        results.size() == 2
    }

    def 'should throw exception when can not find game in repository with gameId when getting results'(){
        when:
        service.getGameResults('wrong-game-id')

        then:
        GameRepository.GameNotFoundException ex = thrown()
        ex.message == 'Game with id: [wrong-game-id] not found.'
    }

    def 'should not be possible get results from game which is not ended'(){
        given:
        Game game = service.createGame()
        Player player = service.addPlayer(game.id, 'Adam')
        service.startGame(game.id)

        when:
        service.getGameResults(game.id)

        then:
        thrown(GameException)
    }

    static class GameRepositoryMock implements GameRepository {
        private final Map<String, Game> repository = new HashMap<>()

        @Override
        synchronized void save(Game game) {
            repository.put(game.getId(), game)
        }

        @Override
        synchronized Optional<Game> findById(String id) {
            return Optional.ofNullable(repository.get(id))
        }

        synchronized Map<String, Game> getRepository() {
            return repository
        }
    }

    static class PlayerRepositoryMock implements PlayerRepository {
        private final Map<String, Player> repository = new HashMap<>()

        @Override
        void save(Player player) {
            repository.put(player.getId(), player)
        }

        @Override
        Optional<Player> findById(String id) {
            return Optional.ofNullable(repository.get(id))
        }

        synchronized Map<String, Player> getRepository() {
            return repository
        }
    }
}
