package com.trzewik.spring.domain.board;

import com.trzewik.spring.domain.game.Game;
import com.trzewik.spring.domain.game.GameException;
import com.trzewik.spring.domain.game.GameFactory;
import com.trzewik.spring.domain.game.GameRepository;
import com.trzewik.spring.domain.game.Result;
import com.trzewik.spring.domain.player.Player;
import com.trzewik.spring.domain.player.PlayerFactory;
import com.trzewik.spring.domain.player.PlayerRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
class BoardImpl implements Board {
    private GameRepository gameRepo;
    private PlayerRepository playerRepo;

    @Override
    public Game createGame() {
        Game game = GameFactory.createGame();
        gameRepo.save(game);
        return game;
    }

    @Override
    public Game addPlayer(String gameId, String playerName) throws GameException, GameRepository.GameNotFoundException {
        Game game = gameRepo.findGame(gameId);

        Player player = PlayerFactory.createPlayer(playerName);
        playerRepo.save(player);
        game.addPlayer(player);

        return game;
    }

    @Override
    public Game startGame(String gameId) throws GameRepository.GameNotFoundException, GameException {
        Game game = gameRepo.findGame(gameId);
        return game.startGame();
    }

    @Override
    public Game makeMove(String gameId, String playerId, Game.Move move) throws GameRepository.GameNotFoundException, PlayerRepository.PlayerNotFoundException, GameException {
        Game game = gameRepo.findGame(gameId);
        Player player = playerRepo.findPlayer(playerId);
        return game.auction(player, move);
    }

    @Override
    public List<Result> getGameResults(String gameId) throws GameRepository.GameNotFoundException, GameException {
        Game game = gameRepo.findGame(gameId);
        return game.getResults();
    }
}
