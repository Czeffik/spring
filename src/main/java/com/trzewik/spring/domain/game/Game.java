package com.trzewik.spring.domain.game;

import com.trzewik.spring.domain.player.Player;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Game {
    private final @NonNull String id;
    private final @NonNull Set<GamePlayer> players;
    private final @NonNull String croupierId;
    private final @NonNull Deck deck;
    private @NonNull Status status;
    private String currentPlayerId;

    @SneakyThrows
    public Game(@NonNull Player croupier) {
        this.id = UUID.randomUUID().toString();
        this.players = new LinkedHashSet<>();
        this.croupierId = croupier.getId();
        this.deck = new Deck();
        this.status = Status.NOT_STARTED;

        addPlayer(croupier);
    }

    public Game startGame() throws GameException {
        if (gameStarted()) {
            throw new GameException("Game started, can not start again");
        }
        if (players.size() <= 1) {
            throw new GameException("Please add at least one player before start.");
        }
        dealCards();
        status = Status.STARTED;
        setCurrentPlayerId();
        return this;
    }

    public void addPlayer(Player player) throws GameException {
        if (gameStarted()) {
            throw new GameException("Game started, can not add new player");
        }
        players.add(GamePlayerFactory.create(player));
    }

    public Game auction(String playerId, @NonNull Move move) throws GameException {
        if (!gameStarted()) {
            throw new GameException("Game NOT started, please start game before auction");
        }
        if (gameEnded()) {
            throw new GameException("Game finished. Now you can check results!");
        }
        if (notPlayerTurn(playerId)) {
            throw new GameException(String.format("Waiting for move from player: [%s] instead of: [%s]",
                getCurrentPlayerId(), playerId));
        }

        getCurrentPlayer().setMove(move);

        if (move.equals(Move.HIT)) {
            getCurrentPlayer().addCard(deck.take());
        }

        setCurrentPlayerId();

        if (getCurrentPlayerId() == null) {
            endGame();
        }

        return this;
    }

    public GamePlayer getCurrentPlayer() {
        return players.stream()
            .filter(p -> p.getId().equals(getCurrentPlayerId()))
            .findFirst()
            .orElse(null);
    }

    public GamePlayer getCroupier() {
        return players.stream()
            .filter(p -> p.getId().equals(getCroupierId()))
            .findFirst()
            .orElse(null);
    }

    public List<Result> getResults() throws GameException {
        if (gameEnded()) {
            return ResultsHelper.createResults(players);
        }
        throw new GameException("Results are available only when game finished. Please continue auction.");
    }

    private void endGame() {
        croupierPicks();
        getCroupier().setMove(Move.STAND);
        status = Status.ENDED;
    }

    private void croupierPicks() {
        while (getCroupier().handValue() < 17) {
            getCroupier().addCard(deck.take());
        }
    }

    private boolean gameStarted() {
        return !status.equals(Status.NOT_STARTED);
    }

    private boolean gameEnded() {
        return status.equals(Status.ENDED);
    }

    private boolean notPlayerTurn(String playerId) {
        return !playerId.equals(getCurrentPlayerId());
    }

    private void setCurrentPlayerId() {
        currentPlayerId = getPlayersWithoutCroupier().stream()
            .filter(p -> !p.isLooser())
            .filter(p -> !Move.STAND.equals(p.getMove()))
            .map(GamePlayer::getId)
            .findFirst()
            .orElse(null);
    }

    private void dealCards() {
        IntStream.range(0, 2).forEach(index -> {
            players.forEach(p -> p.addCard(deck.take()));
        });
    }

    private List<GamePlayer> getPlayersWithoutCroupier() {
        return players.stream()
            .filter(p -> !p.getId().equals(getCroupierId()))
            .collect(Collectors.toList());
    }
}
