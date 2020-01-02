package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.deck.Deck;
import com.trzewik.spring.domain.player.Player;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@AllArgsConstructor
class GameImpl implements Game {
    private final List<Player> players = new ArrayList<>();
    private final @NonNull String id;
    private final @NonNull Player croupier;
    private final @NonNull Deck deck;
    private @NonNull Status status;
    private Player currentPlayer;

    @Override
    public Game startGame() throws GameException {
        if (gameStarted()) {
            throw new GameException("Game started, can not start again");
        }
        if (players.size() == 0) {
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
    public Game auction(Player player, @NonNull Move move) throws GameException {
        if (!gameStarted()) {
            throw new GameException("Game NOT started, please start game before auction");
        }
        if (gameEnded()) {
            throw new GameException(String.format("Game finished. Check results: %s", getResults()));
        }
        if (notPlayerTurn(player)) {
            throw new GameException(String.format("Waiting for move from player: [%s] instead of: [%s]",
                currentPlayer.getId(), player.getId()));
        }
        if (player.getMove() == Move.STAND) {
            throw new GameException(String.format("Player [%s] can not make move. He stands.", player.getId()));
        }

        player.setMove(move);

        if (move.equals(Move.HIT)) {
            player.addCard(deck.take());
        }

        setCurrentPlayer();

        if (currentPlayer == null) {
            endGame();
        }

        return this;
    }

    @Override
    public List<Result> getResults() throws GameException {
        if (gameEnded()) {
            return PlayersResultsHelper.createResults(getPlayersWithCroupier());
        }
        throw new GameException("Results are available only when game finished. Please continue auction.");
    }

    private void endGame() {
        croupierPicks();
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

    private boolean notPlayerTurn(Player player) {
        return !player.equals(currentPlayer);
    }

    private void setCurrentPlayer() {
        currentPlayer = players.stream()
            .filter(player -> !player.isLooser())
            .filter(player -> !Move.STAND.equals(player.getMove()))
            .findFirst()
            .orElse(null);
    }

    private void dealCards() {
        IntStream.range(0, 2).forEach(index -> {
            getPlayersWithCroupier().forEach(player -> {
                player.addCard(deck.take());
            });
        });
    }

    private List<Player> getPlayersWithCroupier() {
        List<Player> allPlayers = new ArrayList<>(players);
        allPlayers.add(croupier);
        return allPlayers;
    }
}
