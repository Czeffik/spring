package com.trzewik.spring.domain.game;

import com.google.common.collect.ImmutableList;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Getter
@ToString
@EqualsAndHashCode
public class Game {
    private static final int MAXIMUM_PLAYERS = 5;
    private final @NonNull String id;
    private final @NonNull Deck deck;
    private final @NonNull List<PlayerInGame> players;
    private final @NonNull String croupierId;
    private final @NonNull Status status;

    public Game(@NonNull final String id,
                @NonNull final Deck deck,
                @NonNull final List<PlayerInGame> players,
                @NonNull final String croupierId,
                @NonNull final Status status
    ) {
        this.id = id;
        this.deck = deck;
        this.players = ImmutableList.copyOf(players);
        this.croupierId = croupierId;
        this.status = status;
    }

    Game(@NonNull final PlayerInGame croupier) {
        this.id = UUID.randomUUID().toString();
        this.deck = new Deck();
        this.players = ImmutableList.<PlayerInGame>builder()
            .add(croupier)
            .build();
        this.croupierId = croupier.getPlayerId();
        this.status = Status.NOT_STARTED;
    }

    private Game(@NonNull final Game game) {
        this.id = game.getId();
        this.deck = game.getDeck();
        this.players = game.getPlayers();
        this.croupierId = game.getCroupierId();
        this.status = game.getStatus();
    }

    private Game(
        @NonNull final Game game,
        @NonNull final List<PlayerInGame> players,
        @NonNull final Status status
    ) {
        this.id = game.getId();
        this.deck = game.getDeck();
        this.players = players;
        this.croupierId = game.getCroupierId();
        this.status = status;
    }

    public PlayerInGame getCroupier() {
        return players.stream()
            .filter(p -> p.getPlayerId().equals(croupierId))
            .findFirst()
            .orElseThrow(() -> new Game.RuntimeException("Croupier not found in game."));
    }

    public PlayerInGame getCurrentPlayer() {
        return players.stream()
            .filter(p -> !p.equals(getCroupier()))
            .filter(p -> !p.isLooser())
            .filter(p -> Move.isNotStand(p.getMove()))
            .findFirst()
            .orElse(null);
    }

    Game addPlayer(@NonNull final PlayerInGame player) throws Exception {
        validatePlayerAddition(player);

        return new Game(this, createPlayersWith(player), this.getStatus());
    }

    private List<PlayerInGame> createPlayersWith(final PlayerInGame player) {
        return ImmutableList.<PlayerInGame>builder()
            .addAll(this.players)
            .add(player)
            .build();
    }

    Game start() throws Exception {
        validateStart();

        return new Game(this, dealCards(), Status.STARTED);
    }

    private List<PlayerInGame> dealCards() {
        return players.stream()
            .map(this::addCard)
            .map(this::addCard)
            .collect(ImmutableList.toImmutableList());
    }

    private PlayerInGame addCard(final PlayerInGame player) {
        return player.addCard(takeCard());
    }

    Game auction(@NonNull final String playerId, @NonNull final Move move) throws Exception {
        validateAuction(playerId);

        return makeMove(move);
    }

    private Game makeMove(final Move move) {
        final PlayerInGame currentPlayer = getCurrentPlayer();
        final PlayerInGame playerAfterMove = makeMove(currentPlayer, move);
        return new Game(this, replacePlayer(currentPlayer, playerAfterMove), this.getStatus());
    }

    private PlayerInGame makeMove(final PlayerInGame currentPlayer, final Move move) {
        final PlayerInGame playerWithChangedMove = currentPlayer.changeMove(move);
        if (Move.isHit(move)) {
            return playerWithChangedMove.addCard(takeCard());
        }
        return playerWithChangedMove;
    }

    private List<PlayerInGame> replacePlayer(final PlayerInGame current, final PlayerInGame updated) {
        final int index = this.players.indexOf(current);
        final List<PlayerInGame> copy = new ArrayList<>(this.players);
        copy.set(index, updated);

        return ImmutableList.<PlayerInGame>builder()
            .addAll(copy)
            .build();
    }

    Game end() {
        if (getCurrentPlayer() == null && status.started()) {
            final PlayerInGame croupierAfterDrawing = croupierDrawCards(this.getCroupier()).changeMove(Move.STAND);

            return new Game(
                this,
                replacePlayer(this.getCroupier(), croupierAfterDrawing),
                Status.ENDED
            );
        }
        return new Game(this);
    }

    private PlayerInGame croupierDrawCards(final PlayerInGame croupier) {
        if (croupier.handValue() < 17) {
            return croupierDrawCards(croupier.addCard(takeCard()));
        }
        return croupier;
    }

    private Card takeCard() {
        return deck.take();
    }

    private void validatePlayerAddition(final PlayerInGame player) throws Exception {
        if (status.isStarted()) {
            throw new Exception("Game started, can not add new player");
        }
        if (players.contains(player)) {
            throw new Exception(String.format("Player: [%s] already added to game!", player));
        }
        if (players.size() >= MAXIMUM_PLAYERS) {
            throw new Exception(String.format("Game is full with: [%s] players. Can not add more players!",
                players.size()));
        }
    }

    private void validateStart() throws Exception {
        if (status.isStarted()) {
            throw new Exception("Game started, can not start again");
        }
        if (players.size() <= 1) {
            throw new Exception("Please add at least one player before start.");
        }
    }

    private void validateAuction(final String playerId) throws Exception {
        if (!status.isStarted()) {
            throw new Exception("Game NOT started, please start game before auction");
        }
        if (status.isEnded()) {
            throw new Exception("Game finished. Now you can check results!");
        }
        if (isNotPlayerTurn(playerId)) {
            throw new Exception(String.format("Waiting for move from player: [%s] instead of: [%s]",
                getCurrentPlayer().getPlayerId(), playerId));
        }
    }

    private boolean isNotPlayerTurn(final String playerId) {
        return !playerId.equals(getCurrentPlayer().getPlayerId());
    }

    public enum Move {
        HIT, STAND;

        static boolean isNotStand(final Move move) {
            return !STAND.equals(move);
        }

        static boolean isHit(final Move move) {
            return HIT.equals(move);
        }
    }

    public enum Status {
        NOT_STARTED, STARTED, ENDED;

        boolean isEnded() {
            return this.equals(ENDED);
        }

        boolean isStarted() {
            return !this.equals(NOT_STARTED);
        }

        boolean started() {
            return this.equals(STARTED);
        }
    }

    public static class Exception extends java.lang.Exception {
        public Exception(String msg) {
            super(msg);
        }
    }

    public static class RuntimeException extends java.lang.RuntimeException {
        public RuntimeException(String msg) {
            super(msg);
        }
    }
}
