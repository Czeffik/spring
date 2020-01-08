package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.common.PlayerGameRepository;
import com.trzewik.spring.domain.result.Result;
import com.trzewik.spring.domain.player.Player;
import com.trzewik.spring.domain.player.PlayerFactory;
import com.trzewik.spring.domain.player.PlayerRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
class GameServiceImpl implements GameService {
    private final GameRepository gameRepo;
    private final PlayerRepository playerRepo;
    private final PlayerGameRepository playerGameRepo;

    @Override
    public Game createGame() {
        Game game = GameFactory.createGame();
        Player croupier = game.getCroupier();

        playerRepo.save(croupier);
        gameRepo.save(game);
        playerGameRepo.save(croupier, game.getId());

        return game;
    }

    @Override
    public Player addPlayer(String gameId, String playerName)
        throws GameException, GameRepository.GameNotFoundException {
        Game game = gameRepo.findGame(gameId);

        Player player = PlayerFactory.createPlayer(playerName);
        game.addPlayer(player);

        playerRepo.save(player);
        playerGameRepo.save(player, gameId);

        return player;
    }

    @Override
    public Game startGame(String gameId) throws GameRepository.GameNotFoundException, GameException {
        Game game = gameRepo.findGame(gameId).startGame();

        gameRepo.update(game);
        playerGameRepo.update(game.getPlayers(), gameId);
        playerGameRepo.update(game.getCroupier(), gameId);

        return game;
    }

    @Override
    public Game makeMove(String gameId, String playerId, Game.Move move)
        throws GameRepository.GameNotFoundException, GameException {
        Game game = gameRepo.findGame(gameId);
        Player player = game.getCurrentPlayer();
        game.auction(playerId, move);

        gameRepo.update(game);
        playerGameRepo.update(player, gameId);
        playerGameRepo.update(game.getCroupier(), gameId);

        return game;
    }

    @Override
    public List<Result> getGameResults(String gameId) throws GameRepository.GameNotFoundException, GameException {
        Game game = gameRepo.findGame(gameId);
        return game.getResults();
    }
}
