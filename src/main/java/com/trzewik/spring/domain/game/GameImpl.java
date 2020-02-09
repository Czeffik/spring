package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.common.Deck;
import com.trzewik.spring.domain.player.Player;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AllArgsConstructor
@EqualsAndHashCode
class GameImpl implements Game {
    private final @NonNull String id;
    private final @NonNull List<Player> players;
    private final @NonNull Player croupier;
    private final @NonNull Deck deck;
    private @NonNull Status status;
    private Player currentPlayer;

    @Override
    public Game startGame() throws GameException {
        if (gameStarted()) {
            throw new GameException("Game started, can not start again");
        }
        if (players.size() <= 1) {
            throw new GameException("Please add at least one player before start.");
        }
        deck.shuffle();
        dealCards();
        status = Status.STARTED;
        setCurrentPlayer();
        return this;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void addPlayer(Player player) throws GameException {
        if (gameStarted()) {
            throw new GameException("Game started, can not add new player");
        }
        players.add(player);
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Game auction(String playerId, @NonNull Move move) throws GameException {
        if (!gameStarted()) {
            throw new GameException("Game NOT started, please start game before auction");
        }
        if (gameEnded()) {
            throw new GameException("Game finished. Now you can check results!");
        }
        if (notPlayerTurn(playerId)) {
            throw new GameException(String.format("Waiting for move from player: [%s] instead of: [%s]",
                currentPlayer.getId(), playerId));
        }

        currentPlayer.setMove(move);

        if (move.equals(Move.HIT)) {
            currentPlayer.addCard(deck.take());
        }

        setCurrentPlayer();

        if (currentPlayer == null) {
            endGame();
        }

        return this;
    }

    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public String getCurrentPlayerId() {
        if (currentPlayer == null) {
            return null;
        }
        return currentPlayer.getId();
    }

    @Override
    public Player getCroupier() {
        return croupier;
    }

    @Override
    public String getCroupierId() {
        return croupier.getId();
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public Deck getDeck() {
        return deck;
    }

    @Override
    public List<Result> getResults() throws GameException {
        if (gameEnded()) {
            return ResultsHelper.createResults(players);
        }
        throw new GameException("Results are available only when game finished. Please continue auction.");
    }

    private void endGame() {
        croupierPicks();
        croupier.setMove(Move.STAND);
        status = Status.ENDED;
    }

    private void croupierPicks() {
        while (croupier.handValue() < 17) {
            croupier.addCard(deck.take());
        }
    }

    private boolean gameStarted() {
        return !status.equals(Status.NOT_STARTED);
    }

    private boolean gameEnded() {
        return status.equals(Status.ENDED);
    }

    private boolean notPlayerTurn(String playerId) {
        return !playerId.equals(currentPlayer.getId());
    }

    private void setCurrentPlayer() {
        currentPlayer = getPlayersWithoutCroupier().stream()
            .filter(player -> !player.isLooser())
            .filter(player -> !Move.STAND.equals(player.getMove()))
            .findFirst()
            .orElse(null);
    }

    private void dealCards() {
        IntStream.range(0, 2).forEach(index -> {
            players.forEach(player -> {
                player.addCard(deck.take());
            });
        });
    }

    private List<Player> getPlayersWithoutCroupier() {
        return players.stream()
            .filter(player -> !player.equals(croupier))
            .collect(Collectors.toList());
    }
}
